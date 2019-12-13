package com.ace.homework2.view.ui.boards

import androidx.recyclerview.widget.DiffUtil
import com.ace.homework2.model.boards.data.Item

class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem::class == newItem::class
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.equals(newItem)
    }
}