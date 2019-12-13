package com.ace.homework2.model.cards.data

import com.ace.homework2.model.boards.data.Board
import com.ace.homework2.model.boards.data.Lists
import com.ace.homework2.model.members.data.Member
import java.io.Serializable

data class Card(
    val desc: String = "",
    val id: String = "",
    val board: Board? = null,
    val idList: String = "",
    val list: Lists? = null,
    val name: String = "",
    val members: List<Member> = emptyList(),
    val idMembers: List<String> = emptyList(),
    val pos: String = "",
    val attachments: List<Attachments> = emptyList()
) : Serializable

data class Attachments(
    val id: String,
    val name: String,
    val mimeType: String,
    val previews: List<Preview>
)

data class Preview(
    val id: String,
    val url: String
)