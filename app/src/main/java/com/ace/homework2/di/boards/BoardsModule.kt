package com.ace.homework2.di.boards

import android.content.Context
import com.ace.homework2.model.network.ApiHolder
import com.ace.homework2.model.boards.BoardApiInterface
import com.ace.homework2.model.prefs.AppPreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BoardsModule {

    @Singleton
    @Provides
    fun provideBoardsApiHelper(): BoardApiInterface {
        return ApiHolder.retrofit.create(BoardApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabaseHelper(context: Context): AppPreferencesHelper {
        return AppPreferencesHelper(context)
    }
}