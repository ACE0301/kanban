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
        val context = applicationContext
        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .storageModule(context?.let { StorageModule(it) })
            .build()

        Stetho.initializeWithDefaults(this)
        OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()
    }
}