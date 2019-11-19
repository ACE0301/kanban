package com.ace.homework2.di

import android.content.Context
import com.ace.homework2.model.network.ApiInterface
import com.ace.homework2.model.network.TrelloHolder
import com.ace.homework2.model.prefs.AppPreferencesHelper
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideApiHelper(): ApiInterface {
        return Retrofit.Builder()
            .baseUrl(TrelloHolder.REST_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.newThread()))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabaseHelper(context: Context): AppPreferencesHelper {
        return AppPreferencesHelper(context)
    }
}


