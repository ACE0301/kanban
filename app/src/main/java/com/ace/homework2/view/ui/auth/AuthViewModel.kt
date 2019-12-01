package com.ace.homework2.view.ui.auth

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.model.network.TrelloHolder
import com.ace.homework2.model.prefs.AppPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    val appPreferencesHelper: AppPreferencesHelper
) : ViewModel() {

    private var disposableSaveToken: Disposable? = null

    private val _successAuthorization = MutableLiveData<Boolean>()
    val successAuthorization: LiveData<Boolean> = _successAuthorization

    fun authorization(url: String) {
        val uri = Uri.parse(url)
        val oauthVerifier = uri.getQueryParameter("oauth_verifier")
        val accessToken =
            TrelloHolder.service.getAccessTokenAsync(TrelloHolder.requestToken.get(), oauthVerifier)
        val token = accessToken.get().token
        disposableSaveToken?.dispose()
        disposableSaveToken = appPreferencesHelper.saveToken(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _successAuthorization.value = true
                }, {
                    _successAuthorization.value = false
                }
            )
    }

    override fun onCleared() {
        disposableSaveToken?.dispose()
    }
}


