package com.ace.homework2.di.history

import androidx.lifecycle.ViewModel
import com.ace.homework2.di.ViewModelKey
import com.ace.homework2.view.ui.details.DetailsViewModel
import com.ace.homework2.view.ui.history.HistoryViewModel
import com.ace.homework2.view.ui.members.MembersViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HistoryViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    internal abstract fun bindMembersViewModel(viewModel: HistoryViewModel): ViewModel
}