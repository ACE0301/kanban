package com.ace.homework2.model.actions.data

import com.ace.homework2.model.boards.data.Board
import com.ace.homework2.model.boards.data.Lists
import com.ace.homework2.model.cards.data.Card
import com.ace.homework2.model.members.data.Member

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
    val attachment: Attachment?
)

data class Old(
    val desc: String? = ""
)

data class Attachment(
    val id: String,
    val name: String,
    val previewUrl: String?
)

data class ActionsPresModel(
    val memberCreator: Member,
    val actionInfo: String,
    val data: Data,
    val type: String,
    val id: String,
    val date: String,
    val previewUrl: String
)