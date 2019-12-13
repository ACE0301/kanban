package com.ace.homework2.view.ui.members

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
import com.ace.homework2.model.cards.data.Card
import com.ace.homework2.model.members.data.Member
import kotlinx.android.synthetic.main.fragment_members.*
import kotlinx.android.synthetic.main.item_board_member.view.*
import javax.inject.Inject

class MembersFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        const val TAG = "MembersFragment"
        private const val ARGUMENT_BOARD_ID = "ARGUMENT_BOARD_ID"
        private const val ARGUMENT_CARD = "ARGUMENT_CARD"
        fun newInstance(boardId: String?, card: Card) = MembersFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_BOARD_ID, boardId)
                putSerializable(ARGUMENT_CARD, card)
            }
        }
    }

    val boardId: String by lazy {
        arguments?.getString(ARGUMENT_BOARD_ID).orEmpty()
    }
    private val card: Card by lazy {
        arguments?.getSerializable(ARGUMENT_CARD) as Card
    }
    lateinit var membersViewModel: MembersViewModel
    private val membersAdapter = BoardMembersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_members, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        membersViewModel = ViewModelProvider(this, viewModelFactory)[MembersViewModel::class.java]

        rvBoardMembers.layoutManager = LinearLayoutManager(context)
        rvBoardMembers.adapter = membersAdapter
        if (membersViewModel.boardMembers.value.isNullOrEmpty()) {
            membersViewModel.loadBoardMembers(boardId)
        }
        membersViewModel.boardMembers.observe(viewLifecycleOwner, Observer {
            membersAdapter.data = Pair(it, card.members)
        })
        membersViewModel.errorMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
        membersViewModel.memberAddedToCardEvent.observe(viewLifecycleOwner, Observer { event ->
            if (event == true) Toast.makeText(
                context,
                getString(R.string.success_adding_member_to_card_toast),
                Toast.LENGTH_LONG
            ).show()
        })
        membersViewModel.memberRemovedToCardEvent.observe(viewLifecycleOwner, Observer { event ->
            if (event == true) Toast.makeText(
                context,
                getString(R.string.success_removing_member_from_card_toast),
                Toast.LENGTH_LONG
            ).show()
        })

        btnOk.setOnClickListener {
            activity?.onBackPressed()
        }

        membersAdapter.onItemClickListener = { view: View, member: Member ->
            if (view.ivCheckCardMember.visibility == View.VISIBLE) {
                view.ivCheckCardMember.visibility = View.GONE
                membersViewModel.removeCardMember(card.id, member.id)
            } else {
                view.ivCheckCardMember.visibility = View.VISIBLE
                membersViewModel.addCardMember(card.id, member.id)
            }
        }
    }
}