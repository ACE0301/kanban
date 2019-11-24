package com.ace.homework2.view.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ace.homework2.model.network.ApiInterface
import javax.inject.Inject


class HistoryViewModel @Inject constructor(
    val apiHelper: ApiInterface
) : ViewModel(

) {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

}