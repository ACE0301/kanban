package com.ace.homework2.view.ui.details

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.R
import com.ace.homework2.model.cards.data.Card
import com.ace.homework2.model.detail.sources.cloud.DetailsApiInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    val detailsApiInterface: DetailsApiInterface,
    val context: Context
) : ViewModel() {

    private var disposeLoadDetails: Disposable? = null

    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val card = MutableLiveData<Card>()

    fun loadDetails(cardId: String) {
        disposeLoadDetails?.dispose()
        disposeLoadDetails = detailsApiInterface.getCardDetails(
            cardId,
            "name,desc",
            true,
            "name",
            true,
            "name",
            true,
            "fullName,initials,avatarHash,username",
            true
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loading.value = true }
            .doFinally { loading.value = false }
            .subscribe(
                { card ->
                    card.members.forEach { member ->
                        if (member.avatar != null) {
                            member.avatar = context.getString(R.string.avatar, member.avatar)
                        }
                    }
                    this.card.value = card

                }, {
                    errorMessage.value = it.message
                }
            )
    }

    override fun onCleared() {
        disposeLoadDetails?.dispose()
    }
}