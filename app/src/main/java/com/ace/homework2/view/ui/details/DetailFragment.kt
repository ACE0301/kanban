package com.ace.homework2.view.ui.details

import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ace.homework2.model.Card
import com.ace.homework2.model.SpecificBoard
import com.github.scribejava.core.model.OAuthConstants.TOKEN
import com.google.android.material.snackbar.Snackbar
import com.woxthebox.draglistview.BoardView
import kotlinx.android.synthetic.main.board_layout.*
import kotlinx.android.synthetic.main.column_header.view.*
import kotlinx.android.synthetic.main.footer_item.view.*
import kotlinx.android.synthetic.main.include_progress_overlay.*
import java.util.*


class DetailFragment : Fragment() {

    companion object {
        const val TAG = "DetailFragment"
        private const val ARGUMENT_BOARD_ID = "ARGUMENT_BOARD_ID"
        fun newInstance(boardId: String?) = DetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_BOARD_ID, boardId)
            }
        }
    }

    private var token: String? = null
    private var sCreatedItems = 0
    private lateinit var mBoardView: BoardView
    private var board: SpecificBoard? = null
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var inAnimation: AlphaAnimation
    private lateinit var outAnimation: AlphaAnimation
    var column = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.ace.homework2.R.layout.board_layout, container, false)
        detailViewModel = ViewModelProvider(this)
            .get(DetailViewModel::class.java)
        mBoardView = view.findViewById(com.ace.homework2.R.id.board_view)
        mBoardView.setSnapToColumnsWhenScrolling(true)
        mBoardView.setSnapToColumnWhenDragging(true)
        mBoardView.setSnapDragItemToTouch(true)
        mBoardView.setSnapToColumnInLandscape(false)
        mBoardView.setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER)
        mBoardView.setBoardListener(object : BoardView.BoardListener {
            override fun onItemDragStarted(column: Int, row: Int) {}
            override fun onItemDragEnded(
                fromColumn: Int,
                fromRow: Int,
                toColumn: Int,
                toRow: Int
            ) {
            }

            override fun onItemChangedPosition(
                oldColumn: Int,
                oldRow: Int,
                newColumn: Int,
                newRow: Int
            ) {
                //TODO пофиксить все баги с перетаскиванием
//                Toast.makeText(context, "Position changed - newColumn: $newColumn newRow: $newRow", Toast.LENGTH_SHORT)
//                    .show()//проверяю куда перенес карточку
                //Toast.makeText(context, "oldColumn: $oldColumn oldRow: $oldRow", Toast.LENGTH_SHORT).show()//проверяю откуда перенес карточку
                var map: MutableMap<String, List<Card>?> = mutableMapOf()
                board?.lists?.forEach {
                    map[it.id] = null
                }
                val map2 = board?.cards?.groupBy {
                    it.idList
                }
                map.entries.forEach { map ->
                    map2?.entries?.forEach { map2 ->
                        if (map.key == map2.key) {
                            map.setValue(map2.value)
                        }
                    }

                }
                val oldListId = board?.lists?.get(oldColumn)?.id
                val newListId = board?.lists?.get(newColumn)?.id
                var newCardPos: Float? = null
                //если колонка не изменилась
                if (oldColumn == newColumn) {
                    //1 элемент в колонке
                    if (newColumn == 0 && newRow == 0) {
                        return
                    } else if (map[newListId]?.size == 1) {
                        return
                        //перетаскиваем в начало колонки
                    } else if (newRow == 0) {
                        newCardPos = ((map[newListId]?.get(newRow)?.pos)?.toFloat())?.div(2)
                        //перетаскиваем в конец колонки
                    } else if (newRow + 1 == map[newListId]?.size!!) {
                        newCardPos = ((map[newListId]?.get(newRow)?.pos)?.toFloat())?.times(2)
                        //перетаскиваем между любых элементов
                    } else {
                        if (oldRow > newRow) {
                            newCardPos =
                                ((map[newListId]?.get(newRow - 1)?.pos)?.toFloat()?.plus(
                                    map[newListId]?.get(
                                        newRow
                                    )?.pos?.toFloat()!!
                                ))?.div(
                                    2
                                )
                        } else {
                            newCardPos =
                                ((map[newListId]?.get(newRow)?.pos)?.toFloat()?.plus(
                                    map[newListId]?.get(
                                        newRow + 1
                                    )?.pos?.toFloat()!!
                                ))?.div(
                                    2
                                )
                        }
                    }
                    //если колонка изменилась
                } else {
                    //если в новой колонке нет элементов
                    if (map[newListId]?.size == null) {
                        newCardPos = 16384.0f
                    }
                    //перетаскиваем в новую колонку в начало
                    else if (newRow == 0) {
                        newCardPos = ((map[newListId]?.get(newRow)?.pos)?.toFloat())?.div(2)
                    }
                    //перетаскиваем в новую колонку в конец
                    else if (newRow == map[newListId]?.size!!) {
                        newCardPos = ((map[newListId]?.get(newRow - 1)?.pos)?.toFloat())?.times(2)
                    } else {
                        if (oldColumn > newColumn) {
                            newCardPos =
                                ((map[newListId]?.get(newRow - 1)?.pos)?.toFloat()?.plus(
                                    map[newListId]?.get(
                                        newRow
                                    )?.pos?.toFloat()!!
                                ))?.div(
                                    2
                                )
                        } else {
                            newCardPos =
                                ((map[newListId]?.get(newRow - 1)?.pos)?.toFloat()?.plus(
                                    map[newListId]?.get(
                                        newRow
                                    )?.pos?.toFloat()!!
                                ))?.div(
                                    2
                                )
                        }

                    }
                }
                //Toast.makeText(context, "columnId: $columnId", Toast.LENGTH_SHORT).show()//проверяю id новой колонки
                //айдишник элемента, который мы перетащили
                var cardId = map[oldListId]?.get(oldRow)?.id
                //Toast.makeText(context, "cardId: $cardId", Toast.LENGTH_SHORT).show()//id карты, которую изменяю
                //Toast.makeText(context, "newCardPos $newCardPos", Toast.LENGTH_SHORT).show()//проверяю новый pos карточки
                Toast.makeText(
                    context,
                    "карточка ${map[oldListId]?.get(oldRow)?.name} " +
                            "из ${if (oldColumn == 0) "первой" else if (oldColumn == 1) "второй" else "последней"} колонки " +
                            "и позиции $oldRow переехала в ${if (newColumn == 0) "первую" else if (newColumn == 1) "вторую" else "последнюю"} колонку на позицию $newRow",
                    Toast.LENGTH_LONG
                ).show()//проверяю новый pos карточки

                Log.d(
                    "M_DetailFragment",
                    "карточка ${cardId?.toUpperCase()} из колонки $oldColumn и позиции $oldRow переехала в колонку $newColumn на позицию $newRow" +
                            "newCardPos $newCardPos  newListId $newListId newColumn $newColumn"
                )
                detailViewModel.updateCard(
                    cardId!!,
                    "$newCardPos",
                    newListId ?: "",
                    context?.getSharedPreferences(TOKEN, MODE_PRIVATE)?.getString(TOKEN, "") ?: ""
                )
                // Toast.makeText(context, "cardId1 $cardId1 cardId2 $cardId2", Toast.LENGTH_SHORT).show()
            }

            override fun onItemChangedColumn(oldColumn: Int, newColumn: Int) {}
            override fun onFocusedColumnChanged(oldColumn: Int, newColumn: Int) {}
            override fun onColumnDragStarted(position: Int) {}
            override fun onColumnDragChangedPosition(oldPosition: Int, newPosition: Int) {}
            override fun onColumnDragEnded(position: Int) {}
        })
        mBoardView.setBoardCallback(object : BoardView.BoardCallback {
            override fun canDragItemAtPosition(column: Int, dragPosition: Int) = true
            override fun canDropItemAtPosition(
                oldColumn: Int,
                oldRow: Int,
                newColumn: Int,
                newRow: Int
            ) = true
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.title = board?.name
        detailViewModel.getToken()
        detailViewModel.token.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            token = it
            detailViewModel.loadCards(token ?: "", arguments?.getString(ARGUMENT_BOARD_ID) ?: "")
        })
        detailViewModel.loading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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

        detailViewModel.board.observe(viewLifecycleOwner, androidx.lifecycle.Observer { it ->
            board = it
            resetBoard()
            toolbar.title = board?.name
        })
        detailViewModel.showCreatedCardEvent.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                if (it == true) { // Observed state is true.
                    Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        getString(com.ace.homework2.R.string.created_card),
                        Snackbar.LENGTH_SHORT // How long to display the message.
                    ).show()
                    detailViewModel.doneShowingSnackbar()// Reset state to make sure the snackbar is only shown once, even if the device has a configuration change.
                }
            })
        detailViewModel.showUpdatedCardEvent.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                if (it == true) { // Observed state is true.
                    Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        getString(com.ace.homework2.R.string.updated_card),
                        Snackbar.LENGTH_SHORT // How long to display the message.
                    ).show()
                    detailViewModel.doneShowingSnackbar()// Reset state to make sure the snackbar is only shown once, even if the device has a configuration change.
                }
            })
    }

    private fun resetBoard() {
        mBoardView.clearBoard()
        for (i in board?.lists!!) {
            addColumn(i.name, i.id)
        }
    }

    private fun addColumn(name: String, idList: String) {
        val mItemArray = ArrayList<Pair<Long, String>>()
        board?.cards?.forEach {
            val id = sCreatedItems++.toLong()
            if (it.idList == idList) {
                mItemArray.add(Pair(id, it.name))
            }
        }
        val listAdapter = DetailAdapter(
            mItemArray,
            com.ace.homework2.R.layout.column_item,
            com.ace.homework2.R.id.item_layout,
            true
        )
        val header = View.inflate(activity, com.ace.homework2.R.layout.column_header, null)
        val footer = View.inflate(activity, com.ace.homework2.R.layout.footer_item, null)
        header.tvHeaderName.text = name

        footer.setOnClickListener {
            footer.tvAddCard.visibility = View.GONE
            toolbar.visibility = View.GONE
            rlCustomToolBar.visibility = View.VISIBLE
            footer.llAddNewCard.visibility = View.VISIBLE
        }
        footer.btnSaveNewCard.setOnClickListener {
            footer.llAddNewCard.visibility = View.GONE
            footer.tvAddCard.visibility = View.VISIBLE
            val editText = footer?.findViewById<EditText>(com.ace.homework2.R.id.etNewCardName)
            val name = editText?.text.toString()
            //val name = "колонка ${mBoardView.getColumnOfHeader(header)} номер ${mBoardView.itemCount} "
            val id = sCreatedItems++.toLong()
            val item = Pair(id, name)
            detailViewModel.createCard(name, idList, token ?: "")
            mBoardView.addItem(
                mBoardView.getColumnOfHeader(header),
                mBoardView.getItemCount(mBoardView.getColumnOfHeader(header)),
                item,
                true
            )
        }
        mBoardView.addColumn(listAdapter, header, null, false, LinearLayoutManager(context))
        val parent =
            mBoardView.getRecyclerView(mBoardView.getColumnOfHeader(header)).parent as LinearLayout
        val recyclerView = mBoardView.getRecyclerView(mBoardView.getColumnOfHeader(header))
        var parentParams = parent.layoutParams as LinearLayout.LayoutParams
        parentParams.weight = 1f
        parentParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        recyclerView.layoutParams = parentParams
        recyclerView.setBackgroundColor(Color.GRAY)
        parent.addView(footer)
    }
}
