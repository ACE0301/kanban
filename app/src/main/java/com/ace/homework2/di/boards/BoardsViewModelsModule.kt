package com.ace.homework2.di.boards

import androidx.lifecycle.ViewModel
import com.ace.homework2.di.ViewModelKey
import com.ace.homework2.view.ui.boards.BoardsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BoardsViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(BoardsViewModel::class)
    internal abstract fun bindBoardsViewModel(viewModel: BoardsViewModel): ViewModel
}