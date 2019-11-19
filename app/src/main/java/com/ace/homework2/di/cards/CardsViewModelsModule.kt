package com.ace.homework2.di.cards

import androidx.lifecycle.ViewModel
import com.ace.homework2.di.ViewModelKey
import com.ace.homework2.view.ui.cards.CardsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CardsViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(CardsViewModel::class)
    internal abstract fun bindCardsViewModel(viewModel: CardsViewModel): ViewModel
}