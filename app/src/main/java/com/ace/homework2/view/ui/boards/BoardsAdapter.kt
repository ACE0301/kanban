package com.ace.homework2.view.ui.boards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ace.homework2.R
import com.ace.homework2.model.boards.Board
import com.ace.homework2.model.boards.Category
import com.ace.homework2.model.boards.Item
import com.osome.stickydecorator.ViewHolderStickyDecoration
import kotlinx.android.synthetic.main.item_board.view.*
import kotlinx.android.synthetic.main.item_category.view.*

private const val TYPE_HEADER = 0
private const val TYPE_BOARD = 1

class BoardsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ViewHolderStickyDecoration.Condition {

    var onItemClickListener: ((String) -> Unit) = {}

    private val asyncListDiffer = AsyncListDiffer(
        this,
        ItemDiffCallback()
    )

    fun setData(data: List<Item>) {
        asyncListDiffer.submitList(data)
    }

    private fun getItem(position: Int) = asyncListDiffer.currentList[position]

    override fun isHeader(position: Int) = getItem(position) is Category

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
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
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_board, parent, false)
                BoardViewHolder(view, onItemClickListener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount() = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = getItem(position)
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

