package com.ace.homework2.view.ui.boards

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView

class ItemTouchHelperCallback(
    private val adapter: ItemTouchHelperAdapter
) : ItemTouchHelper.SimpleCallback(UP or DOWN, START or END) {

    override fun onMove(
        recyclerView: RecyclerView, source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (source.itemViewType != target.itemViewType) return false
        return adapter.onItemMove(source.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemDismiss(viewHolder.adapterPosition)
    }
}

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun onItemDismiss(position: Int)
}