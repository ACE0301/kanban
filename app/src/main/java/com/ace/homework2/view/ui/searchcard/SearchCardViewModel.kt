package com.ace.homework2.view.ui.searchcard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.model.cards.Card
import com.ace.homework2.model.cards.CardsApiInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchCardViewModel @Inject constructor(
    val cardsApiInterface: CardsApiInterface
) : ViewModel() {

    private var disposableLoadCards: Disposable? = null

    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val cards = MutableLiveData<List<Card>>()

    fun loadCards(boardId: String) {
        disposableLoadCards?.dispose()
        disposableLoadCards =
            cardsApiInterface.getBoardDetails(
                boardId,
                "all",
                "id,idList,name,pos,desc,idMembers",
                "all",
                "all",
                "id,name",
                true,
                "previews",
                true,
                "all"

            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loading.value = true }
                .doFinally { loading.value = false }
                .subscribe({
                    cards.value = it.cards
                }, {
                    errorMessage.value = it.message
                })
    }

}