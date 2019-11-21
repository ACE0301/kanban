package com.ace.homework2.view.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.model.Card
import com.ace.homework2.model.network.ApiInterface
import com.ace.homework2.model.network.TrelloHolder.REST_CONSUMER_KEY
import com.ace.homework2.model.prefs.AppPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    val apiHelper: ApiInterface,
    val appPreferencesHelper: AppPreferencesHelper
) : ViewModel(

) {
    init {
        getToken()
    }

    private var disposableGetToken: Disposable? = null
    private var disposeLoadDetails: Disposable? = null

    private var _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _card = MutableLiveData<Card>()
    val card: LiveData<Card> = _card

    fun getToken() {
        disposableGetToken?.dispose()
        disposableGetToken = appPreferencesHelper.getToken()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _token.value = it
                }, {
                    _errorMessage.value = it.message
                }
            )
    }

    fun loadDetails(cardId: String) {
        disposeLoadDetails?.dispose()
        disposeLoadDetails =
            apiHelper.getCardDetails(cardId, REST_CONSUMER_KEY, token.value ?: "", "name")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _card.value = it
                    }, {
                        _errorMessage.value = it.message
                    }
                )


    }
}