package com.ace.homework2.view.ui.searchcard

import androidx.recyclerview.widget.DiffUtil
import com.ace.homework2.model.cards.Card

class SearchCardDiffCallback : DiffUtil.ItemCallback<Card>() {
    override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem == newItem
    }
}