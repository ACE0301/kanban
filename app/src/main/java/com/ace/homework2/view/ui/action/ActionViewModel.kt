package com.ace.homework2.view.ui.action

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.R
import com.ace.homework2.model.actions.Action
import com.ace.homework2.model.actions.ActionApiInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ActionViewModel @Inject constructor(
    val actionApiInterface: ActionApiInterface,
    val context: Context
) : ViewModel() {
    private var disposeLoadHistory: Disposable? = null

    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val actions = MutableLiveData<List<Action>>()

    fun loadHistory(cardId: String) {
        disposeLoadHistory?.dispose()
        disposeLoadHistory = actionApiInterface.getActions(
            cardId,
            "addMemberToCard,createCard,addAttachmentToCard,updateCard"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loading.value = true }
            .doFinally { loading.value = false }
            .subscribe({ actionList ->
                actionList.map {
                    if (it.memberCreator.avatar != null) {
                        it.memberCreator.avatar =
                            context.getString(R.string.avatar, it.memberCreator.avatar)
                    }
                }
                actions.value = actionList.filterNot { action ->
                    action.type == "updateCard" && action.data.card.desc == null
                }
            }, {
                errorMessage.value = it.localizedMessage
            })
    }

    override fun onCleared() {
        disposeLoadHistory?.dispose()
    }
}