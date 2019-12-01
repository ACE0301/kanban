package com.ace.homework2.di.action

import com.ace.homework2.model.network.ApiHolder
import com.ace.homework2.model.actions.ActionApiInterface
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ActionModule {

    @Singleton
    @Provides
    fun provideActionApiHelper(): ActionApiInterface {
        return ApiHolder.retrofit.create(ActionApiInterface::class.java)
    }
}