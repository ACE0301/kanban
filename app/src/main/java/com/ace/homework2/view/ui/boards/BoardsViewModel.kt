package com.ace.homework2.view.ui.boards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.TFSApplication.Companion.appComponent
import com.ace.homework2.model.Board
import com.ace.homework2.model.Category
import com.ace.homework2.model.network.ApiInterface
import com.ace.homework2.model.network.TrelloHolder.REST_CONSUMER_KEY
import com.ace.homework2.model.prefs.AppPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BoardsViewModel : ViewModel() {

    init {
        appComponent.inject(this)
    }

    private var token: String? = null

    @Inject
    lateinit var apiHelper: ApiInterface
    @Inject
    lateinit var appPreferencesHelper: AppPreferencesHelper

    private val _items = MutableLiveData<MutableList<Board>>()
    val items: LiveData<MutableList<Board>> = _items

    private val _board = MutableLiveData<Board>()
    val board: LiveData<Board> = _board

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private var _hasToken = MutableLiveData<Boolean>()
    val hasToken: LiveData<Boolean> = _hasToken

    private var _showRemovedCardEvent = MutableLiveData<Boolean>()
    val showRemovedCardEvent: LiveData<Boolean> = _showRemovedCardEvent

    private var disposableGetToken: Disposable? = null
    private var disposableGetBoards: Disposable? = null
    private var disposablePostNewBoard: Disposable? = null
    private var disposableRemoveBoard: Disposable? = null

    fun doneShowingSnackbar() {
        _showRemovedCardEvent.value = false
    }

    fun getToken() {
        disposableGetToken?.dispose()
        disposableGetToken = appPreferencesHelper.getToken()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    token = it
                    _hasToken.value = true
                }, {
                    _hasToken.value = false
                }
            )
    }

    fun loadBoards() {
        disposableGetBoards?.dispose()
        disposableGetBoards =
            apiHelper.getBoards(true, "id,name,organization", REST_CONSUMER_KEY, token ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _loading.value = true }
                .doFinally { _loading.value = false }
                .subscribe(
                    { it ->
                        it.map {
                            if (it.organization == null) {
                                it.organization =
                                    Category(displayName = "Персональные доски", name = "")
                            }
                        }
                        it.sortBy {
                            it.organization.name
                        }
                        _items.value = it
                    }, {

                    })
    }

    fun createBoard(name: String, organizationName: String) {
        disposablePostNewBoard?.dispose()
        disposablePostNewBoard =
            apiHelper.postBoard(name, organizationName, true, REST_CONSUMER_KEY, token ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _board.value = it
                    }, {

                    }
                )
    }

    fun removeBoard(idBoard: String) {
        disposableRemoveBoard?.dispose()
        disposableRemoveBoard =
            apiHelper.removeBoard(idBoard, REST_CONSUMER_KEY, token ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _showRemovedCardEvent.value = true
                    }, {
                        _showRemovedCardEvent.value = false
                    }
                )

    }

    override fun onCleared() {
        disposableGetToken?.dispose()
        disposableGetBoards?.dispose()
        disposablePostNewBoard?.dispose()
        disposableRemoveBoard?.dispose()
        super.onCleared()
    }
}