package com.ace.homework2.view.ui.boards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.model.boards.Board
import com.ace.homework2.model.boards.Category
import com.ace.homework2.model.boards.BoardApiInterface
import com.ace.homework2.model.network.TrelloHolder.REST_CONSUMER_KEY
import com.ace.homework2.model.prefs.AppPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BoardsViewModel @Inject constructor(
    val boardApiHelper: BoardApiInterface,
    val appPreferencesHelper: AppPreferencesHelper
) : ViewModel() {

    private var _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    private val _items = MutableLiveData<MutableList<Board>>()
    val items: LiveData<MutableList<Board>> = _items

    private val _board = MutableLiveData<Board>()
    val board: LiveData<Board> = _board

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private var disposableGetToken: Disposable? = null
    private var disposableGetBoards: Disposable? = null
    private var disposablePostNewBoard: Disposable? = null
    private var disposableRemoveBoard: Disposable? = null

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

    fun loadBoards() {
        disposableGetBoards?.dispose()
        disposableGetBoards =
            boardApiHelper.getBoards(true, "id,name,organization", REST_CONSUMER_KEY, token.value ?: "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _loading.value = true }
                .doFinally { _loading.value = false }
                .subscribe(
                    { it ->
                        it.map {
                            if (it.organization == null) {
                                it.organization =
                                    Category(
                                        displayName = "Персональные доски",
                                        name = "",
                                        id = ""
                                    )
                            }
                        }
                        it.sortBy {
                            it.organization.name
                        }
                        _items.value = it
                    }, {
                        _errorMessage.value = it.message
                    })
    }

    fun createBoard(name: String, organizationName: String) {
        disposablePostNewBoard?.dispose()
        disposablePostNewBoard =
            boardApiHelper.createBoard(
                name,
                organizationName,
                true,
                REST_CONSUMER_KEY,
                token.value ?: ""
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _board.value = it
                    }, {
                        _errorMessage.value = it.message
                    }
                )
    }

    override fun onCleared() {
        disposableGetToken?.dispose()
        disposableGetBoards?.dispose()
        disposablePostNewBoard?.dispose()
        disposableRemoveBoard?.dispose()
    }
}