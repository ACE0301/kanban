package com.ace.homework2.view.ui.action

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.R
import com.ace.homework2.model.actions.sources.cloud.ActionApiInterface
import com.ace.homework2.model.actions.data.ActionsPresModel
import com.ace.homework2.model.actions.mapper.ActionDataToActionsPresModel
import com.ace.homework2.model.actions.mapper.ActionDataToActionsPresModelImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ActionViewModel @Inject constructor(
    val actionApiInterface: ActionApiInterface,
    val context: Context
) : ViewModel() {
    private var disposeLoadHistory: Disposable? = null

    private val mapper: ActionDataToActionsPresModel = ActionDataToActionsPresModelImpl()

    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val actions = MutableLiveData<List<ActionsPresModel>>()

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
                actionList.map { action ->
                    if (action.memberCreator.avatar != null) {
                        action.memberCreator.avatar =
                            context.getString(R.string.avatar, action.memberCreator.avatar)
                    }
                }
                actions.value = mapper.map(context, actionList)
            }, {
                errorMessage.value = it.message
            })
    }

    override fun onCleared() {
        disposeLoadHistory?.dispose()
    }
}