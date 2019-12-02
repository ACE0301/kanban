package com.ace.homework2.view.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.model.cards.Card
import com.ace.homework2.model.detail.DetailsApiInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    val detailsApiInterface: DetailsApiInterface
) : ViewModel() {

    private var disposeLoadDetails: Disposable? = null

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _card = MutableLiveData<Card>()
    val card: LiveData<Card> = _card

    fun loadDetails(cardId: String) {
        disposeLoadDetails?.dispose()
        disposeLoadDetails = detailsApiInterface.getCardDetails(
            cardId,
            "name,desc",
            true,
            "name",
            true,
            "name",
            true,
            "fullName,initials,avatarHash,username",
            true
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _loading.value = true }
            .doFinally { _loading.value = false }
            .subscribe(
                {
                    _card.value = it
                }, {
                    _errorMessage.value = it.message
                }
            )
    }

    override fun onCleared() {
        disposeLoadDetails?.dispose()
    }
}