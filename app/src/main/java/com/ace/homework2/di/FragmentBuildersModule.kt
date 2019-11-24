package com.ace.homework2.di

import com.ace.homework2.di.auth.AuthViewModelsModule
import com.ace.homework2.di.boards.BoardsViewModelsModule
import com.ace.homework2.di.cards.CardsViewModelsModule
import com.ace.homework2.di.details.DetailsViewModelsModule
import com.ace.homework2.di.history.HistoryViewModelsModule
import com.ace.homework2.di.members.MembersViewModelsModule
import com.ace.homework2.view.ui.auth.AuthFragment
import com.ace.homework2.view.ui.boards.BoardsFragment
import com.ace.homework2.view.ui.cards.CardsFragment
import com.ace.homework2.view.ui.details.DetailsFragment
import com.ace.homework2.view.ui.history.HistoryFragment
import com.ace.homework2.view.ui.members.MembersFragment
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

    @ContributesAndroidInjector(modules = [DetailsViewModelsModule::class])
    @FragmentScope
    abstract fun contributeDetailsFragment(): DetailsFragment

    @ContributesAndroidInjector(modules = [MembersViewModelsModule::class])
    @FragmentScope
    abstract fun contributeMembersFragment(): MembersFragment

    @ContributesAndroidInjector(modules = [HistoryViewModelsModule::class])
    @FragmentScope
    abstract fun contributeHistoryFragment(): HistoryFragment
}