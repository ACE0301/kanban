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
import kotlinx.android.synthetic.main.fragment_members.*
import kotlinx.android.synthetic.main.nav_layout.*
import javax.inject.Inject

class MembersFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var boardId: String
    lateinit var membersViewModel: MembersViewModel
    private val membersAdapter = BoardMembersAdapter()

    companion object {
        const val TAG = "MembersFragment"
        private const val ARGUMENT_BOARD_ID = "ARGUMENT_BOARD_ID"
        fun newInstance(boardId: String?) = MembersFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_BOARD_ID, boardId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_members, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        boardId = arguments?.getString(ARGUMENT_BOARD_ID) ?: ""
        membersViewModel = ViewModelProvider(this, viewModelFactory)[MembersViewModel::class.java]
        rvBoardMembers.layoutManager = LinearLayoutManager(context)
        rvBoardMembers.adapter = membersAdapter
        if (membersViewModel.boardMembers.value.isNullOrEmpty()) {
            membersViewModel.loadBoardMembers(boardId)
        }
        membersViewModel.boardMembers.observe(viewLifecycleOwner, Observer {
            membersAdapter.data = it
        })
        membersViewModel.errorMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

}