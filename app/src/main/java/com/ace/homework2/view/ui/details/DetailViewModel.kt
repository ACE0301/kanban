package com.ace.homework2.view.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.TFSApplication.Companion.appComponent
import com.ace.homework2.model.SpecificBoard
import com.ace.homework2.model.network.ApiInterface
import com.ace.homework2.model.network.TrelloHolder.REST_CONSUMER_KEY
import com.ace.homework2.model.prefs.AppPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailViewModel : ViewModel() {

    init {
        appComponent.inject(this)
    }

    @Inject
    lateinit var apiHelper: ApiInterface
    @Inject
    lateinit var appPreferencesHelper: AppPreferencesHelper

    private var disposableGetToken: Disposable? = null
    private var disposableLoadCards: Disposable? = null
    private var disposableCreateCard: Disposable? = null
    private var disposableUpdateCard: Disposable? = null

    private val _board = MutableLiveData<SpecificBoard>()
    val board: LiveData<SpecificBoard> = _board

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private var _showCreatedCardEvent = MutableLiveData<Boolean>()
    val showCreatedCardEvent: LiveData<Boolean> = _showCreatedCardEvent

    private var _showUpdatedCardEvent = MutableLiveData<Boolean>()
    val showUpdatedCardEvent: LiveData<Boolean> = _showUpdatedCardEvent

    fun doneShowingSnackbar() {
        _showCreatedCardEvent.value = false
        _showUpdatedCardEvent.value = false
    }

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    fun getToken() {
        disposableGetToken?.dispose()
        disposableGetToken = appPreferencesHelper.getToken()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _token.value = it
                }, {

                }
            )
    }

    fun loadCards(token: String, boardId: String) {
        disposableLoadCards?.dispose()
        disposableLoadCards =
            apiHelper.getBoardDetails(
                boardId,
                "all",
                "id,idList,name,pos",
                "all",
                REST_CONSUMER_KEY,
                token
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _loading.value = true }
                .doFinally { _loading.value = false }
                .subscribe({
                    _board.value = it
                }, {})
    }

    fun createCard(name: String, idList: String, token: String) {
        disposableCreateCard?.dispose()
        disposableCreateCard = apiHelper.createCard(name, idList, REST_CONSUMER_KEY, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _showCreatedCardEvent.value = true
            }, {})
    }

    fun updateCard(cardId: String, pos: String, idList: String, token: String) {
        disposableUpdateCard?.dispose()
        disposableUpdateCard = apiHelper.updateCard(cardId, pos, idList, REST_CONSUMER_KEY, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _showUpdatedCardEvent.value = true
            }, {}
            )
    }

    override fun onCleared() {
        disposableGetToken?.dispose()
        disposableLoadCards?.dispose()
        disposableCreateCard?.dispose()
        disposableUpdateCard?.dispose()
        super.onCleared()
    }
}