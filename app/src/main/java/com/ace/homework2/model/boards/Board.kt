package com.ace.homework2.model.boards

import com.ace.homework2.model.cards.Card
import com.ace.homework2.model.members.Member

sealed class Item

data class Board(
    val id: String,
    val name: String,
    var organization: Category,
    val cards: List<Card>,
    val lists: List<Lists>,
    val members: List<Member>
) : Item()

data class Category(
    val id: String = "",
    val displayName: String,
    val name: String = ""
) : Item()

data class Lists(
    val id: String,
    val name: String
)

