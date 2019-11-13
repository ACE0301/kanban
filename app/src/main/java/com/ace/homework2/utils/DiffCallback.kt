package com.ace.homework2.utils

import androidx.recyclerview.widget.DiffUtil
import com.ace.homework2.model.Item

class DiffCallback(private val oldList: List<Item>, private val newList: List<Item>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldList[oldPos]::class == newList[newPos]::class
    }

    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldList[oldPos] == newList[newPos]
    }
}