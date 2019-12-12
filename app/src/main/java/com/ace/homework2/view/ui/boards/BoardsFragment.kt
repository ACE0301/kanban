package com.ace.homework2.view.ui.boards

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ace.homework2.R
import com.ace.homework2.base.BaseFragment
import com.ace.homework2.model.boards.Board
import com.ace.homework2.model.boards.Category
import com.ace.homework2.model.boards.Item
import com.ace.homework2.model.network.token
import com.ace.homework2.view.ui.FragmentView
import com.ace.homework2.view.ui.boards.dialog.NewBoardDialogFragment
import com.ace.homework2.view.ui.cards.CardsFragment
import com.osome.stickydecorator.ViewHolderStickyDecoration
import kotlinx.android.synthetic.main.fragment_boards.*
import javax.inject.Inject

interface OnDialogResult {
    fun onNewBoardAdded(name: String, category: Category)
}

class BoardsFragment : BaseFragment(), OnDialogResult {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var boardsViewModel: BoardsViewModel
    private val boardsAdapter = BoardsAdapter()
    private var items: MutableList<Item> = mutableListOf()

    companion object {
        const val TAG = "BoardsFragment"
        fun newInstance() = BoardsFragment()
        private const val REQUEST_BOARD_NAME = 1
        var hashMap: MutableMap<Category, List<Board>> = hashMapOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_boards, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.title = resources.getString(R.string.boards)
        rvList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = boardsAdapter
            addItemDecoration(ViewHolderStickyDecoration(rvList, boardsAdapter))
        }
        boardsViewModel = ViewModelProvider(this, viewModelFactory)[BoardsViewModel::class.java]

        if (boardsViewModel.token.value.isNullOrEmpty()) {
            boardsViewModel.getToken()
        }
        boardsViewModel.token.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                boardsViewModel.loadBoards()
                token = it
            }
        })
        boardsViewModel.loading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                showLoading()
            } else {
                stopLoading()
            }
        })
        boardsViewModel.items.observe(viewLifecycleOwner, Observer { it ->
            hashMap = it.groupBy {
                it.organization
            }.toMutableMap()

            hashMap.forEach {
                items.add(it.key)
                items.addAll(it.value)
            }
            boardsAdapter.setData(items)
        })

        fab.setOnClickListener {
            val fragment: DialogFragment = NewBoardDialogFragment()
            fragment.setTargetFragment(this, REQUEST_BOARD_NAME)
            fragment.show(parentFragmentManager, fragment.javaClass.name)
        }

        boardsAdapter.onItemClickListener = {
            (activity as? FragmentView)?.openFragmentWithBackstack(
                CardsFragment.newInstance(it),
                CardsFragment.TAG
            )
        }

        boardsViewModel.errorMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onNewBoardAdded(name: String, category: Category) {
        if (name.isEmpty()) {
            Toast.makeText(context, getString(R.string.empty_name_in_dialog), Toast.LENGTH_SHORT)
                .show()
        } else {
            boardsViewModel.createBoard(
                name, category.id
            )
            boardsViewModel.board.observe(this, Observer {
                (activity as? FragmentView)?.openFragmentWithBackstack(
                    CardsFragment.newInstance(it.id),
                    CardsFragment.TAG
                )
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rvList.adapter = null
    }
}

