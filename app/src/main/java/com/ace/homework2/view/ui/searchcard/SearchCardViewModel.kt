package com.ace.homework2.view.ui.searchcard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.model.cards.Card
import com.ace.homework2.model.cards.CardsApiInterface
import com.ace.homework2.model.network.TrelloHolder
import com.ace.homework2.view.ui.boards.token
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchCardViewModel @Inject constructor(
    val cardsApiInterface: CardsApiInterface
) : ViewModel() {

    private var disposableLoadCards: Disposable? = null

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _cards = MutableLiveData<List<Card>>()
    val cards: LiveData<List<Card>> = _cards

    fun loadCards(boardId: String) {
        disposableLoadCards?.dispose()
        disposableLoadCards =
            cardsApiInterface.getBoardDetails(
                boardId,
                "all",
                "id,idList,name,pos,desc,idMembers",
                "all",
                TrelloHolder.REST_CONSUMER_KEY,
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
                .doOnSubscribe { _loading.value = true }
                .doFinally { _loading.value = false }
                .subscribe({
                    _cards.value = it.cards
                }, {
                    _errorMessage.value = it.message
                })
    }

}