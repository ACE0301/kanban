package com.ace.homework2.model

data class SpecificCard(
    val board: Board,
    val id: String,
    val list: Lists,
    val members: List<Member>,
    val name: String
)