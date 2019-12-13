package com.ace.homework2.di.cards

import com.ace.homework2.model.cards.sources.cloud.CardsApiInterface
import com.ace.homework2.model.cards.repository.CardRepository
import com.ace.homework2.model.cards.repository.CardRepositoryFactory
import com.ace.homework2.model.network.ApiHolder
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

    @Singleton
    @Provides
    fun provideRepo(): CardRepository {
        return CardRepositoryFactory.create()
    }
}
