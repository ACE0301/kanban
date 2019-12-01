package com.ace.homework2.di.details

import com.ace.homework2.model.network.ApiHolder
import com.ace.homework2.model.detail.DetailsApiInterface
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DetailsModule {

    @Singleton
    @Provides
    fun provideDetailsApiHelper(): DetailsApiInterface {
        return ApiHolder.retrofit.create(DetailsApiInterface::class.java)
    }
}