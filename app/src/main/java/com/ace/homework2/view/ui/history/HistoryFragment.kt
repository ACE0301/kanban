package com.ace.homework2.view.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ace.homework2.R
import com.ace.homework2.base.BaseFragment
import javax.inject.Inject

class HistoryFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var historyViewModel: HistoryViewModel
    lateinit var cardId: String


    companion object {
        const val TAG = "HistoryFragment"
        private const val ARGUMENT_CARD_ID = "ARGUMENT_CARD_ID"
        fun newInstance(cardId: String?) = HistoryFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_CARD_ID, cardId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_history, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyViewModel = ViewModelProvider(this, viewModelFactory)[HistoryViewModel::class.java]
        cardId = arguments?.getString(ARGUMENT_CARD_ID) ?: ""
    }
}