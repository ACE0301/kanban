package com.ace.homework2.di.cards

import com.ace.homework2.model.network.ApiHolder
import com.ace.homework2.model.cards.CardsApiInterface
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CardModule {

    @Singleton
    @Provides
    fun provideCardsApiHelper(): CardsApiInterface {
        return ApiHolder.retrofit.create(CardsApiInterface::class.java)
    }
}
