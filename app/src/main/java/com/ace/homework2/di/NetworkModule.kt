package com.ace.homework2.di

import android.content.Context
import com.ace.homework2.model.network.ApiHelper
import com.ace.homework2.model.prefs.AppPreferencesHelper
import com.ace.homework2.view.ui.auth.LoginFragment
import com.ace.homework2.view.ui.boards.BoardsFragment
import com.ace.homework2.view.ui.details.DetailFragment
import dagger.Component
import dagger.Module
import dagger.Provides


@Module
class StorageModule(private val context: Context) {

    @Provides
    fun provideDatabaseHelper(): AppPreferencesHelper {
        return AppPreferencesHelper(context)
    }
}

@Module
class NetworkModule {

    @Provides
    fun provideApiHelper(): ApiHelper {
        return ApiHelper()
    }
}

@Component(modules = [NetworkModule::class, StorageModule::class])
interface AppComponent {
    fun inject(boardsFragment: BoardsFragment)

    fun inject(detailFragment: DetailFragment)

    fun inject(loginFragment: LoginFragment)
}