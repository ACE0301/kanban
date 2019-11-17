package com.ace.homework2.view.ui.boards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ace.homework2.R
import com.ace.homework2.model.Board
import com.ace.homework2.model.Category
import com.ace.homework2.model.Item
import com.ace.homework2.utils.DiffCallback
import kotlinx.android.synthetic.main.item_board.view.*
import kotlinx.android.synthetic.main.item_category.view.*
import java.util.*

private const val TYPE_HEADER = 0
private const val TYPE_BOARD = 1

class BoardsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ItemTouchHelperAdapter {

    var onItemClickListener: ((String) -> Unit) = {}
    var onItemSwipe: ((Item) -> Unit) = {}

    var data: MutableList<Item> = mutableListOf()
        set(value) {
            val callback = DiffCallback(field, value)
            field = value
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        }

    override fun onItemDismiss(position: Int) {
        onItemSwipe.invoke(data[position])
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(data, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }


    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is Board -> TYPE_BOARD
            is Category -> TYPE_HEADER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_category, parent, false)
                HeaderViewHolder(view)
            }
            TYPE_BOARD -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_board, parent, false)
                BoardViewHolder(view, onItemClickListener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = data[position]
        when {
            holder is HeaderViewHolder && event is Category -> holder.bindData(event)
            holder is BoardViewHolder && event is Board -> holder.bindData(event)
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(item: Category) {
            itemView.tvCategory.text = item.displayName
        }
    }

    class BoardViewHolder(itemView: View, private val listener: ((String) -> Unit)) :
        RecyclerView.ViewHolder(itemView) {
        fun bindData(item: Board) {
            itemView.tvBoard.text = item.name
            itemView.setOnClickListener {
                listener.invoke(item.id)
            }
        }
    }

}

