package com.ace.homework2.view.ui.cards

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ace.homework2.R
import com.ace.homework2.base.BaseFragment
import com.ace.homework2.model.SpecificBoard
import com.ace.homework2.view.ui.details.DetailsView
import com.ace.homework2.view.ui.members.BoardMembersAdapter
import com.google.android.material.snackbar.Snackbar
import com.woxthebox.draglistview.BoardView
import kotlinx.android.synthetic.main.board_layout.*
import kotlinx.android.synthetic.main.column_footer.view.*
import kotlinx.android.synthetic.main.column_header.view.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.nav_layout.*
import java.util.*
import javax.inject.Inject


class CardsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var cardsViewModel: CardsViewModel
    lateinit var boardId: String
    private var cardCount = 0
    private lateinit var mBoardView: BoardView
    private var board: SpecificBoard? = null //объект доски
    private var isOpenScreen = true //флаг обновления таблицы карточек по открытию экрана
    private val membersAdapter = BoardMembersAdapter()

    companion object {
        const val POS_IF_NO_ELEMENTS = 16384.0f
        const val TAG = "CardsFragment"
        private const val ARGUMENT_BOARD_ID = "ARGUMENT_BOARD_ID"
        fun newInstance(boardId: String?) = CardsFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_BOARD_ID, boardId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.board_layout, container, false)
        boardId = arguments?.getString(ARGUMENT_BOARD_ID) ?: ""
        cardsViewModel = ViewModelProvider(this, viewModelFactory)[CardsViewModel::class.java]
        savedInstanceState?.getBoolean("isOpenScreen", isOpenScreen == false)
        isOpenScreen = true
        cardCount = 0
        mBoardView = view.findViewById(R.id.board_view)
        mBoardView.setSnapToColumnsWhenScrolling(true)
        mBoardView.setSnapToColumnWhenDragging(true)
        mBoardView.setSnapDragItemToTouch(true)
        mBoardView.setSnapToColumnInLandscape(false)
        mBoardView.setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER)
        mBoardView.setBoardListener(object : BoardView.BoardListener {
            override fun onItemDragStarted(column: Int, row: Int) {}
            override fun onItemDragEnded(fromColumn: Int, fromRow: Int, toColumn: Int, toRow: Int) {
                cardsViewModel.onChangePosition(
                    boardId,
                    fromColumn,
                    fromRow,
                    toColumn,
                    toRow
                )

                isOpenScreen = false
                Toast.makeText(
                    context,
                    "из ${if (fromColumn == 0) "первой" else if (fromColumn == 1) "второй" else "последней"} колонки " +
                            "и позиции $fromRow переехала в ${if (toColumn == 0) "первую" else if (toColumn == 1) "вторую" else "последнюю"} колонку на позицию $toRow",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onItemChangedPosition(
                oldColumn: Int,
                oldRow: Int,
                newColumn: Int,
                newRow: Int
            ) {
            }

            override fun onItemChangedColumn(oldColumn: Int, newColumn: Int) {}
            override fun onFocusedColumnChanged(oldColumn: Int, newColumn: Int) {}
            override fun onColumnDragStarted(position: Int) {}
            override fun onColumnDragChangedPosition(oldPosition: Int, newPosition: Int) {}
            override fun onColumnDragEnded(position: Int) {}
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (cardsViewModel.board.value?.id.isNullOrEmpty()) {
            cardsViewModel.loadCards(boardId)
        }
        cardsViewModel.loading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) {
                loading()
            } else {
                stopLoading()
            }
        })

        cardsViewModel.board.observe(viewLifecycleOwner, androidx.lifecycle.Observer { it ->
            board = it
            if (isOpenScreen == true) {
                resetBoard()
                toolbar.title = board?.name
                rvListMembers?.layoutManager = LinearLayoutManager(activity)
                rvListMembers?.adapter = membersAdapter
                membersAdapter.data = board?.members ?: listOf()
            }
        })


        cardsViewModel.showCreatedCardEvent.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it == true) { // Observed state is true.
                    Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        getString(com.ace.homework2.R.string.created_card),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    cardsViewModel.doneShowingSnackbar()// Reset state to make sure the snackbar is only shown once, even if the device has a configuration change.
                }
            })
        cardsViewModel.showUpdatedCardEvent.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it == true) { // Observed state is true.
                    Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        getString(com.ace.homework2.R.string.updated_card),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    cardsViewModel.doneShowingSnackbar()
                }
            })
        cardsViewModel.errorMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun resetBoard() {
        mBoardView.clearBoard()
        for (i in board?.lists!!) {
            addColumn(i.name, i.id)
        }
    }

    private fun addColumn(name: String, idList: String) {
        val mCardArray = ArrayList<Pair<Long, String>>()
        board?.cards?.forEach {
            if (it.idList == idList) {
                mCardArray.add(Pair(cardCount++.toLong(), it.name))
            }
        }
        val cardsAdapter = CardsAdapter(
            mCardArray,
            R.layout.column_item,
            R.id.item_layout
        )
        cardsAdapter.onItemClickListener = {
            (activity as? DetailsView)?.openDetailsFragment(board!!.cards[it.toInt()].id)
        }
        val header = View.inflate(activity, R.layout.column_header, null)
        val footer = View.inflate(activity, R.layout.column_footer, null)
        header.tvHeaderName.text = name

        footer.setOnClickListener {
            onAddCardClicked(it, header, idList)
        }
        btnCancel.setOnClickListener {
            cancelAdding(footer)
        }

        mBoardView.addColumn(cardsAdapter, header, null, false, LinearLayoutManager(context))

        val parent =
            mBoardView.getRecyclerView(mBoardView.getColumnOfHeader(header)).parent as LinearLayout
        val recyclerView = mBoardView.getRecyclerView(mBoardView.getColumnOfHeader(header))
        var parentParams = parent.layoutParams as LinearLayout.LayoutParams
        parentParams.weight = 1f
        parentParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        parentParams.setMargins(20, 20, 20, 20)
        recyclerView.layoutParams = parentParams
        parent.setBackgroundColor(Color.LTGRAY)
        parent.addView(footer)
    }

    private fun onAddCardClicked(footer: View, header: View, idList: String) {
        footer.tvAddCard.visibility = View.GONE
        toolbar.visibility = View.GONE
        llCustomToolBar.visibility = View.VISIBLE
        footer.llAddNewCard.visibility = View.VISIBLE
        btnCancel.setOnClickListener {
            cancelAdding(footer)
        }
        btnSave.setOnClickListener {
            saveNewCard(footer, header, idList)
        }

    }

    private fun cancelAdding(view: View) {
        view.tvAddCard.visibility = View.VISIBLE
        toolbar.visibility = View.VISIBLE
        llCustomToolBar.visibility = View.GONE
        view.llAddNewCard.visibility = View.GONE
    }

    private fun saveNewCard(footer: View, header: View, idList: String) {
        footer.tvAddCard.visibility = View.VISIBLE
        toolbar.visibility = View.VISIBLE
        llCustomToolBar.visibility = View.GONE
        footer.llAddNewCard.visibility = View.GONE
        val name = footer.etNewCardName?.text.toString()
        val item = Pair(cardCount++.toLong(), name)
        cardsViewModel.createCard(name, idList)
        mBoardView.addItem(
            mBoardView.getColumnOfHeader(header),
            mBoardView.getItemCount(mBoardView.getColumnOfHeader(header)),
            item,
            true
        )
        footer.etNewCardName?.text?.clear()
    }
}
