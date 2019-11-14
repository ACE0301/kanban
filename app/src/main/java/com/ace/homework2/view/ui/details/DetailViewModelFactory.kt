package com.ace.homework2.view.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ace.homework2.model.network.ApiHelper
import com.ace.homework2.model.prefs.PreferencesHelper

class DetailViewModelFactory(
    private val repository: PreferencesHelper,
    private val apiHelper: ApiHelper
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository, apiHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

