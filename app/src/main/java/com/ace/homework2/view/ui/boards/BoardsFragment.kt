package com.ace.homework2.view.ui.boards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.ace.homework2.TFSApplication.Companion.appComponent
import com.ace.homework2.model.*
import com.ace.homework2.model.network.ApiHelper
import com.ace.homework2.model.prefs.AppPreferencesHelper
import com.ace.homework2.view.custom.CustomViewFragment
import com.ace.homework2.view.ui.details.DetailView
import com.ace.homework2.view.ui.dialog.NewBoardDialogFragment
import kotlinx.android.synthetic.main.fragment_boards.*
import javax.inject.Inject

interface OnDialogResult {
    fun onNewBoardAdded(name: String, category: Category)
}

open class BoardsFragment : Fragment(), OnDialogResult {

    @Inject
    lateinit var apiHelper: ApiHelper
    @Inject
    lateinit var appPreferencesHelper: AppPreferencesHelper

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        appComponent.inject(this)

        //appPreferencesHelper = TFSApplication.getComponent()?.getDatabaseHelper()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(com.ace.homework2.R.layout.fragment_boards, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = boardsAdapter
        }

        val boardsViewModelFactory = BoardsViewModelFactory(appPreferencesHelper, apiHelper)
        boardsViewModel = ViewModelProvider(this, boardsViewModelFactory)
            .get(BoardsViewModel::class.java)

        boardsViewModel.getToken()
        boardsViewModel.token.observe(viewLifecycleOwner, Observer {
            token = it
            boardsViewModel.loadBoards(token ?: "")

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

