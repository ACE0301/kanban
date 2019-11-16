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
import com.ace.homework2.model.*
import com.ace.homework2.view.custom.CustomViewFragment
import com.ace.homework2.view.ui.details.DetailView
import com.ace.homework2.view.ui.dialog.NewBoardDialogFragment
import kotlinx.android.synthetic.main.fragment_boards.*
import kotlinx.android.synthetic.main.include_progress_overlay.*


interface OnDialogResult {
    fun onNewBoardAdded(name: String, category: Category)
}

class BoardsFragment : Fragment(), OnDialogResult {

    companion object {
        const val TAG = "BoardsFragment"
        fun newInstance() = BoardsFragment()
        private const val REQUEST_BOARD_NAME = 1
        var hashMap: MutableMap<Category, List<Board>> = hashMapOf()
    }

    private lateinit var boardsViewModel: BoardsViewModel
    private val boardsAdapter = BoardsAdapter()
    private val mapper: MapToListMapper = MapToListMapperImpl()
    private var items: MutableList<Item> = mutableListOf()
    private var token: String? = null
    private lateinit var inAnimation: AlphaAnimation
    private lateinit var outAnimation: AlphaAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(com.ace.homework2.R.layout.fragment_boards, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = boardsAdapter
        }
        boardsViewModel = ViewModelProvider(this)
            .get(BoardsViewModel::class.java)

        boardsViewModel.getToken()
        boardsViewModel.token.observe(viewLifecycleOwner, Observer {
            token = it
            boardsViewModel.loadBoards(token ?: "")

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
            fragment.show(fragmentManager!!, fragment.javaClass.name)
        }

        btnOpenCustom.setOnClickListener {
            openCustomViewFragment()
        }

        boardsAdapter.onItemClickListener = {
            (activity as? DetailView)?.openDetailFragment(it)
        }
    }

    override fun onNewBoardAdded(name: String, category: Category) {
        if (name.isEmpty()) {
            Toast.makeText(context, getString(com.ace.homework2.R.string.empty_name_in_dialog), Toast.LENGTH_SHORT)
                .show()
        } else {
            boardsViewModel.createBoard(
                name, category.name, token ?: ""
            )
            boardsViewModel.board.observe(this, Observer {
                (activity as? DetailView)?.openDetailFragment(it.id)
            })
        }
    }

    private fun openCustomViewFragment() {
        fragmentManager?.beginTransaction()
            ?.replace(com.ace.homework2.R.id.container, CustomViewFragment.newInstance(), CustomViewFragment.TAG)
            ?.addToBackStack(CustomViewFragment.TAG)
            ?.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rvList.adapter = null
    }

}

