package com.ace.homework2.model.boards

interface MapToListMapper {
    fun map(from: Map<Category, List<Board>>): MutableList<Item>
}

class MapToListMapperImpl : MapToListMapper {

    override fun map(from: Map<Category, List<Board>>): MutableList<Item> {
        val resultList = mutableListOf<Item>()
        for (i in from.entries) {
            resultList.apply {
                if (i.key == null) {
                    add(
                        Category(
                            displayName = "Персональные доски",
                            name = "",
                            id = ""
                        )
                    )
                } else {
                    add(i.key)
                }
                addAll(i.value)
            }
        }
        return resultList
    }
}