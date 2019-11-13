package com.ace.homework2.model

data class SpecificBoard(
    val cards: List<Card>,
    val id: String,
    val lists: List<Lists>,
    val name: String
)