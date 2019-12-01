package com.ace.homework2.di.searchcard

import androidx.lifecycle.ViewModel
import com.ace.homework2.di.ViewModelKey
import com.ace.homework2.view.ui.details.DetailsViewModel
import com.ace.homework2.view.ui.members.MembersViewModel
import com.ace.homework2.view.ui.searchcard.SearchCardViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SearchCardViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchCardViewModel::class)
    internal abstract fun bindMembersViewModel(viewModel: SearchCardViewModel): ViewModel
}