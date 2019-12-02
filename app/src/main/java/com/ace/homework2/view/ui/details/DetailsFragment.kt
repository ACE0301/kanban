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
import com.ace.homework2.model.cards.Card
import com.ace.homework2.view.ui.action.ActionView
import com.ace.homework2.view.ui.members.MembersView
import kotlinx.android.synthetic.main.action_layout.*
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.members_layout.*
import javax.inject.Inject

class DetailsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        const val TAG = "DetailsFragment"
        private const val ARGUMENT_CARD_ID = "ARGUMENT_CARD_ID"
        fun newInstance(cardId: String?) = DetailsFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_CARD_ID, cardId)
            }
        }
    }

    lateinit var detailsViewModel: DetailsViewModel
    private val cardMembersAdapter = CardMembersAdapter()
    private val attachmentsImageAdapter = AttachmentsImageAdapter()
    private val attachmentsFileAdapter = AttachmentsFileAdapter()
    private val cardId: String
        get() = arguments?.getString(ARGUMENT_CARD_ID) ?: ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsViewModel = ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]
        rvCardMembers.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        rvCardMembers.adapter = cardMembersAdapter

        rvImageAttachments.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        rvImageAttachments.adapter = attachmentsImageAdapter

        rvFileAttachments.layoutManager = LinearLayoutManager(context)
        rvFileAttachments.adapter = attachmentsFileAdapter

        detailsViewModel.loadDetails(cardId)
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
            (activity as? MembersView)?.openMembersFragment(
                detailsViewModel.card.value?.board?.id ?: "", detailsViewModel.card.value!!
            )

        }
        btnHistory.setOnClickListener {
            (activity as? ActionView)?.openHistoryFragment(detailsViewModel.card.value?.id ?: "")
        }
    }

    private fun loadData(card: Card) {
        if (card.members.isNotEmpty()) {
            tvMembers.visibility = View.GONE
            cardMembersAdapter.setData(card.members)
        }
        cardName.text = card.name
        boardName.text = card.board?.name
        listName.text = card.list?.name
        if (card.desc.isEmpty()) {
            card_description.text = getString(R.string.empty_description_card_text)
        } else {
            card_description.text = card.desc
        }

        rvImageAttachments.visibility = View.VISIBLE
        attachmentsImageAdapter.setData(card.attachments.filter {
            it.previews.isNotEmpty()
        })
        rvFileAttachments.visibility = View.VISIBLE
        attachmentsFileAdapter.setData(card.attachments.filter {
            it.mimeType != "image/jpeg" && it.mimeType != "image/png"
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rvCardMembers.adapter = null
        rvImageAttachments.adapter = null
        rvFileAttachments.adapter = null
    }
}