package com.ace.homework2.view.ui.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.model.boards.Board
import com.ace.homework2.model.cards.Card
import com.ace.homework2.model.cards.CardsApiInterface
import com.ace.homework2.model.network.TrelloHolder.REST_CONSUMER_KEY
import com.ace.homework2.view.ui.boards.token
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CardsViewModel @Inject constructor(
    val cardsApiInterface: CardsApiInterface
) : ViewModel() {

    var map: MutableMap<String, List<Card>?> =
        mutableMapOf() //главная мапа с колонками и карточками

    private var disposableGetToken: Disposable? = null
    private var disposableLoadCards: Disposable? = null
    private var disposableCreateCard: Disposable? = null
    private var disposableUpdateCard: Disposable? = null

    private val _board = MutableLiveData<Board>()
    val board: LiveData<Board> = _board

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage

    fun loadCards(isShowLoading: Boolean, boardId: String) {
        disposableLoadCards?.dispose()
        disposableLoadCards =
            cardsApiInterface.getBoardDetails(
                boardId,
                "all",
                "id,idList,name,pos,desc,idMembers",
                "all",
                REST_CONSUMER_KEY,
                token,
                "all",
                "id,name",
                true,
                "previews",
                true,
                "all"

            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { if (isShowLoading) _loading.value = true }
                .doFinally { if (isShowLoading) _loading.value = false }
                .subscribe({
                    it.lists.forEach { map[it.id] = null }
                    val map2 = it.cards.groupBy { it.idList }
                    map.entries.forEach { map ->
                        map2.entries.forEach { map2 ->
                            if (map.key == map2.key) {
                                map.setValue(map2.value)
                            }
                        }
                    }
                    _board.value = it
                }, {
                    _errorMessage.value = it.message
                })
    }

    fun createCard(name: String, idList: String) {
        disposableCreateCard?.dispose()
        disposableCreateCard =
            cardsApiInterface.createCard(name, idList, REST_CONSUMER_KEY, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _successMessage.value = "Карточка создана"
                    board.value?.id?.let { loadCards(false, it) }
                }, {
                    _errorMessage.value = it.message
                })
    }

    fun updateCard(cardId: String, pos: String, idList: String) {
        disposableUpdateCard?.dispose()
        disposableUpdateCard =
            cardsApiInterface.updateCard(cardId, pos, idList, REST_CONSUMER_KEY, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _successMessage.value = "Карточка обновлена"
                    board.value?.id?.let { loadCards(false, it) }
                }, {
                    _errorMessage.value = it.message
                }
                )
    }

    fun onChangePosition(
        fromColumn: Int,
        fromRow: Int,
        toColumn: Int,
        toRow: Int
    ) {

        val oldListId = _board.value?.lists?.get(fromColumn)?.id
        val newListId = _board.value?.lists?.get(toColumn)?.id

        var newCardPos: Float?
        //если колонка не изменилась
        if (fromColumn == toColumn) {

            //если позиция не изменилась, то ничего не возвращаем
            if (fromColumn == toColumn && fromRow == toRow) {
                return
            }
            //если перетаскиваем в начало колонки, то берем позицию 1-го элемента и делим на 2
            else if (toRow == 0) {
                newCardPos = ((map[newListId]?.get(toRow)?.pos)?.toFloat())?.div(2)
                //если перетаскиваем в конец колонки, то берем позицию последнего элемента и делим на 2
            } else if (toRow + 1 == map[newListId]?.size!!) {
                newCardPos = ((map[newListId]?.get(toRow)?.pos)?.toFloat())?.times(2)
                //если кладем между 2-х элементов, берем сумму этих элементо и делим на 2(доп.проверка куда двигаем элемент вниз/вверх)
            } else {
                //если переносим снизу вверх
                newCardPos = if (fromRow > toRow) {
                    ((map[newListId]?.get(toRow - 1)?.pos)?.toFloat()?.plus(
                        map[newListId]?.get(
                            toRow
                        )?.pos?.toFloat()!!
                    ))?.div(
                        2
                    )
                    //если переносим сверху вниз
                } else {
                    ((map[newListId]?.get(toRow)?.pos)?.toFloat()?.plus(
                        map[newListId]?.get(toRow + 1)?.pos?.toFloat()!!
                    ))?.div(2)
                }
            }
            //если колонка изменилась
        } else {
            //если в новой колонке нет элементов
            when {
                map[newListId]?.size == null -> newCardPos =
                    CardsFragment.POS_IF_NO_ELEMENTS
                //перетаскиваем в новую колонку в начало
                toRow == 0 -> newCardPos = ((map[newListId]?.get(toRow)?.pos)?.toFloat())?.div(2)
                //перетаскиваем в новую колонку в конец
                toRow == map[newListId]?.size!! -> newCardPos =
                    ((map[newListId]?.get(toRow - 1)?.pos)?.toFloat())?.times(2)
                else -> //если переносим снизу вверх
                    newCardPos = if (fromColumn > toColumn) {
                        ((map[newListId]?.get(toRow - 1)?.pos)?.toFloat()?.plus(
                            map[newListId]?.get(toRow)?.pos?.toFloat()!!
                        ))?.div(
                            2
                        )
                        //если переносим сверху вниз
                    } else {
                        ((map[newListId]?.get(toRow - 1)?.pos)?.toFloat()?.plus(
                            map[newListId]?.get(
                                toRow
                            )?.pos?.toFloat()!!
                        ))?.div(2)
                    }
            }
        }

        //айдишник элемента, который мы перетащили
        var cardId = map[oldListId]?.get(fromRow)?.id

        updateCard(cardId ?: "", "$newCardPos", newListId ?: "")
    }

    override fun onCleared() {
        disposableGetToken?.dispose()
        disposableLoadCards?.dispose()
        disposableCreateCard?.dispose()
        disposableUpdateCard?.dispose()
    }
}