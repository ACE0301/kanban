package com.ace.homework2.model.boards.data

import com.ace.homework2.model.cards.data.Card
import com.ace.homework2.model.members.data.Member

sealed class Item

data class Board(
    val id: String,
    val name: String,
    var organization: Category?,
    val cards: List<Card>,
    val lists: List<Lists>,
    val members: List<Member>
) : Item()

data class Category(
    val id: String = "",
    var displayName: String = "Персональные доски",
    val name: String = ""
) : Item()

data class Lists(
    val id: String,
    val name: String
)

