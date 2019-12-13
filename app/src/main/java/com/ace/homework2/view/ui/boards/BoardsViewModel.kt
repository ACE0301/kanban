package com.ace.homework2.view.ui.boards

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.model.boards.data.Board
import com.ace.homework2.model.boards.sources.cloud.BoardApiInterface
import com.ace.homework2.model.prefs.AppPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BoardsViewModel @Inject constructor(
    val boardApiHelper: BoardApiInterface,
    val appPreferencesHelper: AppPreferencesHelper
) : ViewModel() {

    var token = MutableLiveData<String>()
    val items = MutableLiveData<MutableList<Board>>()
    val board = MutableLiveData<Board>()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

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
                    token.value = it
                }, {
                    errorMessage.value = it.message
                }
            )
    }

    fun loadBoards() {
        disposableGetBoards?.dispose()
        disposableGetBoards =
            boardApiHelper.getBoards(true, "id,name,organization")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loading.value = true }
                .doFinally { loading.value = false }
                .subscribe(
                    { boards ->
                        boards.sortBy { board ->
                            board.organization?.name
                        }
                        items.value = boards
                    }, {
                        errorMessage.value = it.message
                    })
    }

    fun createBoard(name: String, organizationName: String) {
        disposablePostNewBoard?.dispose()
        disposablePostNewBoard =
            boardApiHelper.createBoard(
                name,
                organizationName,
                true
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        board.value = it
                    }, {
                        errorMessage.value = it.message
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