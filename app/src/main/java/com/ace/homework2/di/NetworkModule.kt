package com.ace.homework2.di

import android.content.Context
import com.ace.homework2.model.network.ApiInterface
import com.ace.homework2.model.network.TrelloHolder
import com.ace.homework2.model.prefs.AppPreferencesHelper
import com.ace.homework2.view.ui.auth.LoginViewModel
import com.ace.homework2.view.ui.boards.BoardsViewModel
import com.ace.homework2.view.ui.details.DetailViewModel
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class StorageModule(private val appContext: Context) {


    @Provides
    @Singleton
    fun provideDatabaseHelper(): AppPreferencesHelper {
        return AppPreferencesHelper(appContext)
    }
}

@Module
class NetworkModule {


    @Provides
    @Singleton
    fun provideApiHelper(): ApiInterface {
        return Retrofit.Builder()
            .baseUrl(TrelloHolder.REST_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.newThread()))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiInterface::class.java)
    }
}

@Singleton
@Component(modules = [NetworkModule::class, StorageModule::class])

interface AppComponent {
    fun inject(boardsViewModel: BoardsViewModel)

    fun inject(detailViewModel: DetailViewModel)

    fun inject(loginViewModel: LoginViewModel)
}


