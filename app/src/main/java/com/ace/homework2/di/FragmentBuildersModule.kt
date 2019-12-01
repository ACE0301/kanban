package com.ace.homework2.di

import com.ace.homework2.di.action.ActionViewModelsModule
import com.ace.homework2.di.auth.AuthViewModelsModule
import com.ace.homework2.di.boards.BoardsViewModelsModule
import com.ace.homework2.di.cards.CardsViewModelsModule
import com.ace.homework2.di.details.DetailsViewModelsModule
import com.ace.homework2.di.members.MembersViewModelsModule
import com.ace.homework2.di.searchcard.SearchCardViewModelsModule
import com.ace.homework2.view.ui.action.ActionFragment
import com.ace.homework2.view.ui.auth.AuthFragment
import com.ace.homework2.view.ui.boards.BoardsFragment
import com.ace.homework2.view.ui.cards.CardsFragment
import com.ace.homework2.view.ui.details.DetailsFragment
import com.ace.homework2.view.ui.members.MembersFragment
import com.ace.homework2.view.ui.searchcard.SearchCardFragment
import com.ace.homework2.view.ui.searchcard.SearchCardViewModel
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

    @ContributesAndroidInjector(modules = [ActionViewModelsModule::class])
    @FragmentScope
    abstract fun contributeActionFragment(): ActionFragment

    @ContributesAndroidInjector(modules = [SearchCardViewModelsModule::class])
    @FragmentScope
    abstract fun contributeSearchCardFragment(): SearchCardFragment
}