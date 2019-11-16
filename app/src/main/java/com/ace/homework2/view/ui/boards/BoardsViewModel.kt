package com.ace.homework2.view.ui.boards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.TFSApplication.Companion.appComponent
import com.ace.homework2.model.Board
import com.ace.homework2.model.Category
import com.ace.homework2.model.network.ApiInterface
import com.ace.homework2.model.network.TrelloHolder
import com.ace.homework2.model.prefs.AppPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BoardsViewModel : ViewModel() {

    init {
        appComponent.inject(this)
    }

    @Inject
    lateinit var apiHelper: ApiInterface
    @Inject
    lateinit var appPreferencesHelper: AppPreferencesHelper

    private val _items = MutableLiveData<MutableList<Board>>()
    val items: LiveData<MutableList<Board>> = _items

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    private val _board = MutableLiveData<Board>()
    val board: LiveData<Board> = _board

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private var disposableGetToken: Disposable? = null
    private var disposableGetBoards: Disposable? = null
    private var disposablePostNewBoard: Disposable? = null

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

    fun loadBoards(token: String) {
        disposableGetBoards?.dispose()
        disposableGetBoards =
            apiHelper.getBoards(true, "id,name,organization", TrelloHolder.REST_CONSUMER_KEY, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _loading.value = true }
                .doFinally { _loading.value = false }
                .subscribe(
                    { it ->
                        it.map {
                            if (it.organization == null) {
                                it.organization = Category(displayName = "Персональные доски", name = "")
                            }
                        }
                        it.sortBy {
                            it.organization.name
                        }
                        _items.value = it
                    }, {

                    })
    }

    fun createBoard(name: String, organizationName: String, token: String) {
        disposablePostNewBoard?.dispose()
        disposablePostNewBoard =
            apiHelper.postBoard(name, organizationName, true, TrelloHolder.REST_CONSUMER_KEY, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _board.value = it
                    }, {

                    }
                )
    }

    override fun onCleared() {
        disposableGetToken?.dispose()
        disposableGetBoards?.dispose()
        disposablePostNewBoard?.dispose()
        super.onCleared()
    }

}