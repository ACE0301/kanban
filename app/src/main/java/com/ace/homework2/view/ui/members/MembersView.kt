package com.ace.homework2.view.ui.members

import com.ace.homework2.model.cards.Card

interface MembersView {
    fun openMembersFragment(boardId: String, card: Card)
}