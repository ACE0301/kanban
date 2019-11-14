package com.ace.homework2.view.ui.boards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ace.homework2.model.network.ApiHelper
import com.ace.homework2.model.prefs.PreferencesHelper

class BoardsViewModelFactory(
    private val repository: PreferencesHelper,
    private val apiHelper: ApiHelper
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BoardsViewModel::class.java)) {
            return BoardsViewModel(repository, apiHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

