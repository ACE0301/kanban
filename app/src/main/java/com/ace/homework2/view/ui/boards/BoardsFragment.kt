package com.ace.homework2.view.ui.boards

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.ace.homework2.R
import com.ace.homework2.model.*
import com.ace.homework2.view.ui.auth.AuthViewModel
import com.ace.homework2.view.ui.cards.CardsView
import com.ace.homework2.view.ui.dialog.NewBoardDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_boards.*
import kotlinx.android.synthetic.main.include_progress_overlay.*
import javax.inject.Inject


interface OnDialogResult {
    fun onNewBoardAdded(name: String, category: Category)
}

class BoardsFragment : DaggerFragment(), OnDialogResult {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var boardsViewModel: BoardsViewModel

    companion object {
        const val TAG = "BoardsFragment"
        fun newInstance() = BoardsFragment()
        private const val REQUEST_BOARD_NAME = 1
        var hashMap: MutableMap<Category, List<Board>> = hashMapOf()
    }

    private val boardsAdapter = BoardsAdapter()
    private val mapper: MapToListMapper = MapToListMapperImpl()
    private var items: MutableList<Item> = mutableListOf()
    private lateinit var inAnimation: AlphaAnimation
    private lateinit var outAnimation: AlphaAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_boards, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.title = resources.getString(R.string.boards)
        rvList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = boardsAdapter
        }
        boardsViewModel = ViewModelProvider(this, viewModelFactory)[BoardsViewModel::class.java]

        boardsViewModel.getToken()
        boardsViewModel.token.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                boardsViewModel.loadBoards()
            }
        })
        boardsViewModel.loading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) {
                inAnimation = AlphaAnimation(0f, 1f)
                inAnimation.duration = 200
                progress_overlay.animation = inAnimation
                progress_overlay.visibility = View.VISIBLE
            } else {
                outAnimation = AlphaAnimation(1f, 0f)
                outAnimation.duration = 200
                progress_overlay.animation = outAnimation
                progress_overlay.visibility = View.GONE
            }
        })
        boardsViewModel.items.observe(viewLifecycleOwner, Observer { it ->
            hashMap = it.groupBy {
                it.organization
            }.toMutableMap()
            items = mapper.map(hashMap)
            boardsAdapter.data = items
        })

        val callback = ItemTouchHelperCallback(boardsAdapter as ItemTouchHelperAdapter)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvList)

        fab.setOnClickListener {
            val fragment: DialogFragment = NewBoardDialogFragment()
            fragment.setTargetFragment(this, REQUEST_BOARD_NAME)
            fragment.show(parentFragmentManager, fragment.javaClass.name)
        }

        boardsAdapter.onItemClickListener = {
            (activity as? CardsView)?.openCardsFragment(it)
        }
        boardsAdapter.onItemSwipe = {
            if (it is Board) {
                boardsViewModel.removeBoard(it.id)
            }
        }
        boardsViewModel.showRemovedCardEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.board_is_removed_snackbar),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                boardsViewModel.doneShowingSnackbar()// Reset state to make sure the snackbar is only shown once, even if the device has a configuration change.
            }
        })
        boardsViewModel.errorMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onNewBoardAdded(name: String, category: Category) {
        if (name.isEmpty()) {
            Toast.makeText(
                context,
                getString(R.string.empty_name_in_dialog),
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            boardsViewModel.createBoard(
                name, category.name
            )
            boardsViewModel.board.observe(this, Observer {
                (activity as? CardsView)?.openCardsFragment(it.id)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rvList.adapter = null
    }

}

