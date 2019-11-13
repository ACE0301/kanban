package com.ace.homework2.model.prefs

import android.content.Context
import com.ace.homework2.base.context.ContextDelegate
import com.github.scribejava.core.model.OAuthConstants.TOKEN
import io.reactivex.Completable
import io.reactivex.Single


class AppPreferencesHelper(
    private val contextDelegate: ContextDelegate
) : PreferencesHelper {

    private var preferences = contextDelegate.getContext()?.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)

    override fun saveToken(token: String) = Completable.fromAction {
        contextDelegate.getContext()?.let { context ->
            val editor = preferences?.edit()
            editor?.putString(TOKEN, token)?.apply()
        }
    }

    override fun getToken(): Single<String> {
        return Single.just(preferences?.getString(TOKEN, "") ?: "")
    }

}

