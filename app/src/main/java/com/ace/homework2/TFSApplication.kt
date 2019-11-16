package com.ace.homework2

import android.app.Application
import com.ace.homework2.di.AppComponent
import com.ace.homework2.di.DaggerAppComponent
import com.ace.homework2.di.NetworkModule
import com.ace.homework2.di.StorageModule
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient


class TFSApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .storageModule(StorageModule(this))
            .build()

        Stetho.initializeWithDefaults(this)
        OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()
    }

}