package com.ace.homework2.view.ui.cards

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.setMargins
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ace.homework2.R
import com.ace.homework2.base.BaseFragment
import com.ace.homework2.extentions.hideKeyboard
import com.ace.homework2.model.boards.Board
import com.ace.homework2.model.cards.Card
import com.ace.homework2.view.ui.details.CardMembersAdapter
import com.ace.homework2.view.ui.details.DetailsView
import com.ace.homework2.view.ui.searchcard.SearchCardView
import com.woxthebox.draglistview.BoardView
import kotlinx.android.synthetic.main.column_footer.view.*
import kotlinx.android.synthetic.main.column_header.view.*
import kotlinx.android.synthetic.main.custom_toobar_search_user.*
import kotlinx.android.synthetic.main.custom_toolbar_add_card.*
import kotlinx.android.synthetic.main.members_layout.*
import javax.inject.Inject

class CardsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        const val POS_IF_NO_ELEMENTS = 16384.0f
        const val LINEAR_LAYOUT_WEIGHT = 1f
        const val MARGINS_RECYCLER_VIEW = 20
        const val TAG = "CardsFragment"
        private const val ARGUMENT_BOARD_ID = "ARGUMENT_BOARD_ID"
        fun newInstance(boardId: String?) = CardsFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_BOARD_ID, boardId)
            }
        }
    }

    lateinit var cardsViewModel: CardsViewModel
    private var cardCount = 0
    private lateinit var mBoardView: BoardView
    private val cardMembersAdapter = CardMembersAdapter()
    private val boardId: String
        get() = arguments?.getString(ARGUMENT_BOARD_ID) ?: ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.board_layout, container, false)
        mBoardView = view.findViewById(R.id.board_view)
        mBoardView.setSnapDragItemToTouch(true)
        mBoardView.setBoardListener(object : BoardView.BoardListener {
            override fun onItemChangedColumn(oldColumn: Int, newColumn: Int) {}
            override fun onFocusedColumnChanged(oldColumn: Int, newColumn: Int) {}
            override fun onColumnDragStarted(position: Int) {}
            override fun onItemChangedPosition(
                oldColumn: Int,
                oldRow: Int,
                newColumn: Int,
                newRow: Int
            ) {
            }

            override fun onColumnDragChangedPosition(oldPosition: Int, newPosition: Int) {}
            override fun onColumnDragEnded(position: Int) {}
            override fun onItemDragStarted(column: Int, row: Int) {}
            override fun onItemDragEnded(fromColumn: Int, fromRow: Int, toColumn: Int, toRow: Int) {
                cardsViewModel.onChangePosition(fromColumn, fromRow, toColumn, toRow)
            }
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardsViewModel = ViewModelProvider(this, viewModelFactory)[CardsViewModel::class.java]

        rvCardMembers?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        rvCardMembers?.adapter = cardMembersAdapter

        if (cardsViewModel.board.value?.id.isNullOrEmpty()) {
            cardsViewModel.loadCards(true, boardId)
        }

        cardsViewModel.board.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            loadData(it)
        })

        cardsViewModel.loading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) loading() else stopLoading()
        })

        cardsViewModel.errorMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        btnSearch.setOnClickListener {
            cardsViewModel.board.value?.id?.let { id ->
                (activity as? SearchCardView)?.openSearchCard(
                    id
                )
            }
        }
    }

    private fun loadData(board: Board) {
        cardCount = 0
        tvToolbarBoardName.text = board.name
        mBoardView.clearBoard()
        board.lists.forEach {
            addColumn(it.name, it.id)
        }
    }

    private fun addColumn(name: String, idList: String) {
        val mCardArray = ArrayList<Pair<Long, Card>>()
        cardsViewModel.board.value?.cards?.forEach {
            if (it.idList == idList) {
                mCardArray.add(Pair(cardCount++.toLong(), it))
            }
        }
        val cardsAdapter = CardsAdapter(mCardArray, R.layout.column_item, R.id.item_layout)

        // Открывает экран деталей
        cardsAdapter.onItemClickListener = {
            cardsViewModel.board.value?.cards?.get(it.toInt())?.id?.let { id ->
                (activity as? DetailsView)?.openDetailsFragment(
                    id
                )
            }
        }
        val header = View.inflate(activity, R.layout.column_header, null)
        header.tvHeaderName.text = name

        val footer = View.inflate(activity, R.layout.column_footer, null)
        footer.setOnClickListener {
            onAddCardClicked(it, header, idList)
        }
        btnCancel.setOnClickListener {
            cancelAdding(footer)
        }

        mBoardView.addColumn(cardsAdapter, header, null, false, LinearLayoutManager(context))

        val columnLinearLayout =
            mBoardView.getRecyclerView(mBoardView.getColumnOfHeader(header)).parent as LinearLayout
        columnLinearLayout.setBackgroundColor(Color.LTGRAY)
        columnLinearLayout.addView(footer)
        val recyclerView = mBoardView.getRecyclerView(mBoardView.getColumnOfHeader(header))
        val parentParams = columnLinearLayout.layoutParams as LinearLayout.LayoutParams
        parentParams.apply {
            weight = LINEAR_LAYOUT_WEIGHT
            gravity = Gravity.CENTER_HORIZONTAL
            height = LinearLayout.LayoutParams.WRAP_CONTENT
            setMargins(MARGINS_RECYCLER_VIEW)
        }
        recyclerView.layoutParams = parentParams
    }

    private fun onAddCardClicked(footer: View, header: View, idList: String) {
        footer.tvAddCard.visibility = View.GONE
        boardToolbar.visibility = View.GONE
        llCustomToolBarAddCard.visibility = View.VISIBLE
        footer.llAddNewCard.visibility = View.VISIBLE
        btnCancel.setOnClickListener {
            cancelAdding(footer)
        }
        btnSave.setOnClickListener {
            saveNewCard(footer, header, idList)
            activity?.hideKeyboard()
        }

    }

    private fun cancelAdding(view: View) {
        view.tvAddCard.visibility = View.VISIBLE
        boardToolbar.visibility = View.VISIBLE
        llCustomToolBarAddCard.visibility = View.GONE
        view.llAddNewCard.visibility = View.GONE
    }

    private fun saveNewCard(footer: View, header: View, idList: String) {
        footer.tvAddCard.visibility = View.VISIBLE
        boardToolbar.visibility = View.VISIBLE
        llCustomToolBarAddCard.visibility = View.GONE
        footer.llAddNewCard.visibility = View.GONE
        val name = footer.etNewCardName?.text.toString()
        if (name.isEmpty()) {
            Toast.makeText(context, getString(R.string.empty_card_name_toast), Toast.LENGTH_SHORT)
                .show()
        } else {
            cardsViewModel.createCard(name, idList)
            mBoardView.addItem(
                mBoardView.getColumnOfHeader(header),
                mBoardView.getItemCount(mBoardView.getColumnOfHeader(header)),
                Pair(cardCount++.toLong(), Card(name = name)),
                false
            )
            footer.etNewCardName?.text?.clear()
        }
    }
}
