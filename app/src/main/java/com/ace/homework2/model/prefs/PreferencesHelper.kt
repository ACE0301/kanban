package com.ace.homework2.model.prefs

import io.reactivex.Completable
import io.reactivex.Single

interface PreferencesHelper {
    fun getToken(): Single<String>
    fun saveToken(token: String): Completable
}