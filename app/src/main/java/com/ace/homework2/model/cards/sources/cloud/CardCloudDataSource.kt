package com.ace.homework2.model.cards.sources.cloud

import com.ace.homework2.model.boards.data.Board
import com.ace.homework2.model.network.ApiHolder
import io.reactivex.Completable
import io.reactivex.Single

interface CardCloudDataSource {
    fun getBoardDetails(boardId: String): Single<Board>

    fun createCard(name: String, idList: String): Completable

    fun updateCard(
        cardId: String,
        pos: String,
        idList: String
    ): Completable
}

class CardCloudDataSourceImpl(
    private val api: CardsApiInterface = ApiHolder.retrofit.create(
        CardsApiInterface::class.java)
) : CardCloudDataSource {

    override fun getBoardDetails(boardId: String): Single<Board> =
        api.getBoardDetails(
            boardId,
            "all",
            "id,idList,name,pos,desc,idMembers",
            "all",
            "all",
            "id,name",
            true,
            "previews",
            true,
            "all"
        )

    override fun createCard(name: String, idList: String): Completable =
        api.createCard(name, idList)


    override fun updateCard(cardId: String, pos: String, idList: String): Completable =
        api.updateCard(cardId, pos, idList)
}