package com.ace.homework2.view.ui.details

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
import com.ace.homework2.model.SpecificCard
import com.ace.homework2.view.ui.history.HistoryView
import com.ace.homework2.view.ui.members.CardMembersAdapter
import com.ace.homework2.view.ui.members.MembersView
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject

class DetailsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var detailsViewModel: DetailsViewModel
    lateinit var cardId: String
    private val cardMembersAdapter = CardMembersAdapter()

    companion object {
        const val TAG = "DetailsFragment"
        private const val ARGUMENT_CARD_ID = "ARGUMENT_CARD_ID"
        fun newInstance(cardId: String?) = DetailsFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_CARD_ID, cardId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardId = arguments?.getString(ARGUMENT_CARD_ID) ?: ""
        detailsViewModel = ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]
        if (detailsViewModel.card.value?.id.isNullOrEmpty()) {
            detailsViewModel.loadDetails(cardId)
        }

        detailsViewModel.loading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) {
                loading()
            } else {
                stopLoading()
            }
        })
        detailsViewModel.errorMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
        detailsViewModel.card.observe(viewLifecycleOwner, Observer {
            loadData(it)

        })

        fabAddMember.setOnClickListener {
            detailsViewModel.card.value?.board?.id?.let { id ->
                (activity as? MembersView)?.openMembersFragment(id)
            }
        }
        btnHistory.setOnClickListener {
            detailsViewModel.card.value?.board?.id?.let { id ->
                (activity as? HistoryView)?.openHistoryFragment(id)
            }
        }
    }

    private fun loadData(card: SpecificCard) {
        rvCardMembers.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        rvCardMembers.adapter = cardMembersAdapter
        if (card.members.isNotEmpty()) {
            tvMembers.visibility = View.GONE
            cardMembersAdapter.data = card.members
        }
        cardName.text = card.name
        boardName.text = card.board.name
        listName.text = card.list.name
    }
}