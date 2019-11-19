package com.ace.homework2.di

import com.ace.homework2.di.auth.AuthViewModelsModule
import com.ace.homework2.di.boards.BoardsViewModelsModule
import com.ace.homework2.di.cards.CardsViewModelsModule
import com.ace.homework2.view.ui.auth.AuthFragment
import com.ace.homework2.view.ui.boards.BoardsFragment
import com.ace.homework2.view.ui.cards.CardsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Scope


@Scope
@MustBeDocumented
annotation class FragmentScope

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector(modules = [AuthViewModelsModule::class])
    @FragmentScope
    abstract fun contributeAuthFragment(): AuthFragment

    @ContributesAndroidInjector(modules = [BoardsViewModelsModule::class])
    @FragmentScope
    abstract fun contributeBoardsFragment(): BoardsFragment

    @ContributesAndroidInjector(modules = [CardsViewModelsModule::class])
    @FragmentScope
    abstract fun contributeCardsFragment(): CardsFragment
}