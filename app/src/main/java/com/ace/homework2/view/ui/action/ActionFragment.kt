package com.ace.homework2.view.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ace.homework2.R
import com.ace.homework2.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_actions.*
import javax.inject.Inject

class ActionFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        const val TAG = "ActionFragment"
        private const val ARGUMENT_CARD_ID = "ARGUMENT_CARD_ID"
        fun newInstance(cardId: String?) = ActionFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_CARD_ID, cardId)
            }
        }
    }

    private val historyAdapter = ActionAdapter()
    lateinit var actionViewModel: ActionViewModel
    private val cardId: String
        get() = arguments?.getString(ARGUMENT_CARD_ID) ?: ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_actions, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionViewModel = ViewModelProvider(this, viewModelFactory)[ActionViewModel::class.java]
        rvCardHistory.layoutManager = LinearLayoutManager(context)
        rvCardHistory.adapter = historyAdapter

        if (actionViewModel.actions.value.isNullOrEmpty()) {
            actionViewModel.loadHistory(cardId)
        }
        actionViewModel.actions.observe(viewLifecycleOwner, Observer {
            historyAdapter.setData(it)
        })
        actionViewModel.errorMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        actionViewModel.loading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) loading() else stopLoading()
        })
    }
}