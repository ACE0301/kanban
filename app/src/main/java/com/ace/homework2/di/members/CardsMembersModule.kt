package com.ace.homework2.di.members

import com.ace.homework2.model.network.ApiHolder
import com.ace.homework2.model.members.CardMembersApiInterface
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class CardsMembersModule {

    @Singleton
    @Provides
    fun provideCardsMembersApiHelper(): CardMembersApiInterface {
        return ApiHolder.retrofit.create(CardMembersApiInterface::class.java)
    }
}
