package com.ace.homework2.model.actions

import com.ace.homework2.model.boards.Board
import com.ace.homework2.model.boards.Lists
import com.ace.homework2.model.cards.Card
import com.ace.homework2.model.members.Member

data class Action(
    val data: Data,
    val date: String,
    val id: String,
    val member: Member,
    val memberCreator: Member,
    val type: String
)

data class Data(
    val board: Board,
    val card: Card,
    val old: Old,
    val list: Lists,
    val attachment: Attachment
)

data class Old(
    val desc: String
)

data class Attachment(
    val id: String,
    val name: String,
    val previewUrl: String
)