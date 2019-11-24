package com.ace.homework2.view.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.model.SpecificCard
import com.ace.homework2.model.network.ApiInterface
import com.ace.homework2.model.network.TrelloHolder.REST_CONSUMER_KEY
import com.ace.homework2.view.ui.boards.token
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class DetailsViewModel @Inject constructor(
    val apiHelper: ApiInterface
) : ViewModel(

) {

    private var disposeLoadDetails: Disposable? = null

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _card = MutableLiveData<SpecificCard>()
    val card: LiveData<SpecificCard> = _card

    fun loadDetails(cardId: String) {
        disposeLoadDetails?.dispose()
        disposeLoadDetails = apiHelper.getCardDetails(
            cardId,
            REST_CONSUMER_KEY,
            token,
            "name",
            true,
            "name",
            true,
            "name",
            true,
            "fullName,initials,avatarHash,username"
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

}