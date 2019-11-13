package com.ace.homework2

import android.app.Application
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient

class TFSApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()
    }

}