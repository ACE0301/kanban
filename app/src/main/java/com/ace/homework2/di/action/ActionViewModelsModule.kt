package com.ace.homework2.di.action

import androidx.lifecycle.ViewModel
import com.ace.homework2.di.ViewModelKey
import com.ace.homework2.view.ui.action.ActionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ActionViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(ActionViewModel::class)
    internal abstract fun bindMembersViewModel(viewModel: ActionViewModel): ViewModel
}