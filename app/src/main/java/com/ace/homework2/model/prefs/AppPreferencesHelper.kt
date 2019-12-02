package com.ace.homework2.model.prefs

import android.content.Context
import android.content.SharedPreferences
import com.github.scribejava.core.model.OAuthConstants.TOKEN
import io.reactivex.Completable
import io.reactivex.Single

open class AppPreferencesHelper(
    private val context: Context
) : PreferencesHelper {

    var preferences: SharedPreferences = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)

    override fun saveToken(token: String) = Completable.fromAction {
        context.let { _ ->
            val editor = preferences.edit()
            editor?.putString(TOKEN, token)?.apply()
        }
    }

    override fun getToken(): Single<String> {
        return Single.just(preferences.getString(TOKEN, "") ?: "")
    }
}

