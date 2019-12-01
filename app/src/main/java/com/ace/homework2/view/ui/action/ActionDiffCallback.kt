package com.ace.homework2.view.ui.action

import androidx.recyclerview.widget.DiffUtil
import com.ace.homework2.model.actions.Action

class ActionDiffCallback : DiffUtil.ItemCallback<Action>() {
    override fun areItemsTheSame(oldItem: Action, newItem: Action): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Action, newItem: Action): Boolean {
        return oldItem == newItem
    }
}