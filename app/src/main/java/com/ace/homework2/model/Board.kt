package com.ace.homework2.model

sealed class Item

data class Board(
    val id: String,
    val name: String,
    var organization: Category
) : Item()

data class Category(
    val displayName: String,
    val name: String
) : Item()

