package com.ace.homework2.view.ui.details

import androidx.recyclerview.widget.DiffUtil
import com.ace.homework2.model.cards.data.Attachments

class AttachmentsDiffCallback : DiffUtil.ItemCallback<Attachments>() {
    override fun areItemsTheSame(oldItem: Attachments, newItem: Attachments): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Attachments, newItem: Attachments): Boolean {
        return oldItem == newItem
    }
}