package com.ace.homework2.view.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.model.SpecificBoard
import com.ace.homework2.model.network.ApiHolder
import com.ace.homework2.model.network.TrelloHolder.REST_CONSUMER_KEY
import com.ace.homework2.model.prefs.PreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailViewModel(
    private val repository: PreferencesHelper
) : ViewModel() {

    private var disposableGetToken: Disposable? = null
    private var disposableLoadCards: Disposable? = null
    private var disposableCreateCard: Disposable? = null
    private var disposableUpdateCard: Disposable? = null

    private val _board = MutableLiveData<SpecificBoard>()
    val board: LiveData<SpecificBoard> = _board

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
        disposableGetToken = repository.getToken()
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
            ApiHolder.service.getBoardDetails(
                boardId,
                "all",
                "id,idList,name,pos",
                "all",
                REST_CONSUMER_KEY,
                token
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _board.value = it
                }, {})
    }

    fun createCard(name: String, idList: String, token: String) {
        disposableCreateCard?.dispose()
        disposableCreateCard = ApiHolder.service.createCard(name, idList, REST_CONSUMER_KEY, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _showCreatedCardEvent.value = true
            }, {})
    }

    fun updateCard(cardId: String, pos: String, idList: String, token: String) {
        disposableUpdateCard?.dispose()
        disposableUpdateCard = ApiHolder.service.updateCard(cardId, pos, idList, REST_CONSUMER_KEY, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _showUpdatedCardEvent.value = true
            }, {}
            )
    }
}