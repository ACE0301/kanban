package com.ace.homework2.model.cards.repository

import com.ace.homework2.model.boards.data.Board
import com.ace.homework2.model.cards.sources.cloud.CardCloudDataSource
import com.ace.homework2.model.cards.sources.cloud.CardCloudDataSourceImpl
import io.reactivex.Completable
import io.reactivex.Single

interface CardRepository {
    fun getBoardDetails(boardId: String): Single<Board>

    fun createCard(name: String, idList: String): Completable

    fun updateCard(
        cardId: String,
        pos: String,
        idList: String
    ): Completable
}

object CardRepositoryFactory {
    private val repository by lazy { CardRepositoryImpl() }
    fun create(): CardRepository = repository
}

class CardRepositoryImpl(
    private val cardDataSource: CardCloudDataSource = CardCloudDataSourceImpl()
) : CardRepository {

    override fun getBoardDetails(boardId: String): Single<Board> =
        cardDataSource.getBoardDetails(boardId)

    override fun createCard(name: String, idList: String): Completable =
        cardDataSource.createCard(name, idList)

    override fun updateCard(cardId: String, pos: String, idList: String): Completable =
        cardDataSource.updateCard(cardId, pos, idList)
}