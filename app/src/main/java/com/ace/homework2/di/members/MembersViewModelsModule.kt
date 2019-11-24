package com.ace.homework2.di.members

import androidx.lifecycle.ViewModel
import com.ace.homework2.di.ViewModelKey
import com.ace.homework2.view.ui.details.DetailsViewModel
import com.ace.homework2.view.ui.members.MembersViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MembersViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(MembersViewModel::class)
    internal abstract fun bindMembersViewModel(viewModel: MembersViewModel): ViewModel
}