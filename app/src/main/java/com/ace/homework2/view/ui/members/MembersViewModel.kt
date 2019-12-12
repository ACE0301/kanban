package com.ace.homework2.view.ui.members

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.R
import com.ace.homework2.model.members.CardMembersApiInterface
import com.ace.homework2.model.members.Member
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MembersViewModel @Inject constructor(
    val cardMembersApiInterface: CardMembersApiInterface,
    val context: Context
) : ViewModel() {

    private var disposeGetBoardMembers: Disposable? = null
    private var disposeAddCardMember: Disposable? = null
    private var disposeRemoveCardMember: Disposable? = null

    val boardMembers = MutableLiveData<List<Member>>()
    val errorMessage = MutableLiveData<String>()
    val memberAddedToCardEvent = MutableLiveData<Boolean>()
    val memberRemovedToCardEvent = MutableLiveData<Boolean>()

    fun loadBoardMembers(boardId: String) {
        disposeGetBoardMembers?.dispose()
        disposeGetBoardMembers = cardMembersApiInterface.getBoardMembers(
            boardId, "id,avatarHash,initials,fullName,username"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ members ->
                members.forEach { member ->
                    if (member.avatar != null) {
                        member.avatar = context.getString(
                            R.string.avatar, member.avatar
                        )
                    }
                }
                boardMembers.value = members
            }, {
                errorMessage.value = it.message
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
                    memberAddedToCardEvent.value = true
                }, {
                    errorMessage.value = it.message
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
                memberRemovedToCardEvent.value = true
            }, {
                errorMessage.value = it.message
            })
    }

    override fun onCleared() {
        disposeGetBoardMembers?.dispose()
        disposeAddCardMember?.dispose()
        disposeRemoveCardMember?.dispose()
    }
}