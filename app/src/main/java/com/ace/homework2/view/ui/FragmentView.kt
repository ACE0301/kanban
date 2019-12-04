package com.ace.homework2.view.ui

import com.ace.homework2.model.cards.Card

interface FragmentView {
    fun openHistoryFragment(cardId: String)
    fun openSearchCardFragment(boardId: String)
    fun openMembersFragment(boardId: String, card: Card)
    fun openDetailsFragment(cardId: String)
    fun openCardsFragment(boardId: String)
    fun openBoardsFragment()
}