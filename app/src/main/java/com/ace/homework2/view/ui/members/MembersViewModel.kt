package com.ace.homework2.view.ui.members

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.model.Member
import com.ace.homework2.model.network.ApiInterface
import com.ace.homework2.model.network.TrelloHolder
import com.ace.homework2.view.ui.boards.token
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MembersViewModel @Inject constructor(
    val apiHelper: ApiInterface
) : ViewModel() {

    private var disposeGetBoardMembers: Disposable? = null

    private val _boardMembers = MutableLiveData<List<Member>>()
    val boardMembers: LiveData<List<Member>> = _boardMembers

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadBoardMembers(boardId: String) {
        disposeGetBoardMembers?.dispose()
        disposeGetBoardMembers = apiHelper.getBoardMembers(
            boardId, TrelloHolder.REST_CONSUMER_KEY,
            token, "id,avatarHash,initials,fullName,username"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _boardMembers.value = it
            }, {
                _errorMessage.value = it.message
            })
    }

}