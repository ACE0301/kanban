package com.ace.homework2.view.ui.members

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.model.members.CardMembersApiInterface
import com.ace.homework2.model.members.Member
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MembersViewModel @Inject constructor(
    val cardMembersApiInterface: CardMembersApiInterface
) : ViewModel() {

    private var disposeGetBoardMembers: Disposable? = null
    private var disposeAddCardMember: Disposable? = null
    private var disposeRemoveCardMember: Disposable? = null

    private val _boardMembers = MutableLiveData<List<Member>>()
    val boardMembers: LiveData<List<Member>> = _boardMembers

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _memberAddedToCardEvent = MutableLiveData<Boolean>()
    val memberAddedToCardEvent = _memberAddedToCardEvent

    private val _memberRemovedToCardEvent = MutableLiveData<Boolean>()
    val memberRemovedToCardEvent = _memberRemovedToCardEvent

    fun loadBoardMembers(boardId: String) {
        disposeGetBoardMembers?.dispose()
        disposeGetBoardMembers = cardMembersApiInterface.getBoardMembers(
            boardId, "id,avatarHash,initials,fullName,username"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _boardMembers.value = it
            }, {
                _errorMessage.value = it.message
            })
    }

    fun addCardMember(cardId: String, memberId: String) {
        disposeAddCardMember?.dispose()
        disposeAddCardMember = cardMembersApiInterface.addCardMember(
            cardId,
            memberId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _memberAddedToCardEvent.value = true
                }, {
                    _errorMessage.value = it.message
                }
            )
    }

    fun removeCardMember(cardId: String, memberId: String) {
        disposeRemoveCardMember?.dispose()
        disposeRemoveCardMember = cardMembersApiInterface.removeCardMember(
            cardId,
            memberId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _memberRemovedToCardEvent.value = true
            }, {
                _errorMessage.value = it.message
            })
    }

    override fun onCleared() {
        disposeGetBoardMembers?.dispose()
        disposeAddCardMember?.dispose()
        disposeRemoveCardMember?.dispose()
    }
}