package com.ace.homework2.view.ui.action

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.model.actions.Action
import com.ace.homework2.model.actions.ActionApiInterface
import com.ace.homework2.model.network.TrelloHolder.REST_CONSUMER_KEY
import com.ace.homework2.view.ui.boards.token
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ActionViewModel @Inject constructor(
    val actionApiInterface: ActionApiInterface
) : ViewModel() {
    private var disposeLoadHistory: Disposable? = null

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _actions = MutableLiveData<List<Action>>()
    val actions: LiveData<List<Action>> = _actions

    fun loadHistory(cardId: String) {
        disposeLoadHistory?.dispose()
        disposeLoadHistory = actionApiInterface.getActions(
            cardId,
            "addMemberToCard,createCard,addAttachmentToCard,updateCard"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _loading.value = true }
            .doFinally { _loading.value = false }
            .subscribe({ it ->
                _actions.value = it.filterNot {
                    it.type == "updateCard" && it.data.card.desc == null
                }
            }, {
                _errorMessage.value = it.localizedMessage
            })
    }

    override fun onCleared() {
        disposeLoadHistory?.dispose()
    }
}