package com.ace.homework2.view.ui.action

import androidx.recyclerview.widget.DiffUtil
import com.ace.homework2.model.actions.data.ActionsPresModel

class ActionDiffCallback : DiffUtil.ItemCallback<ActionsPresModel>() {
    override fun areItemsTheSame(oldItem: ActionsPresModel, newItem: ActionsPresModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ActionsPresModel, newItem: ActionsPresModel): Boolean {
        return oldItem == newItem
    }
}