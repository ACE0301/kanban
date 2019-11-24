package com.ace.homework2.model.network

import com.ace.homework2.model.Board
import com.ace.homework2.model.Member
import com.ace.homework2.model.SpecificBoard
import com.ace.homework2.model.SpecificCard
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface ApiInterface {

    @GET("1/members/me/boards")
    fun getBoards(
        @Query("organization") organization: Boolean,
        @Query("fields") fields: String,
        @Query("key") key: String,
        @Query("token") token: String
    ): Single<MutableList<Board>>

    @POST("1/boards/")
    fun postBoard(
        @Query("name") name: String,
        @Query("idOrganization") organizationName: String,
        @Query("defaultLists") defaultLists: Boolean,
        @Query("key") key: String,
        @Query("token") token: String
    ): Single<Board>

    //Получение деталей конкретной доски
    @GET("1/boards/{id}")
    fun getBoardDetails(
        @Path("id") boardId: String,
        @Query("cards") cards: String,
        @Query("card_fields") cardFields: String,
        @Query("lists") lists: String,
        @Query("key") key: String,
        @Query("token") token: String,
        @Query("members") members: String,
        @Query("list_fields") listFields: String
    ): Single<SpecificBoard>

    @POST("1/cards")
    fun createCard(
        @Query("name") name: String,
        @Query("idList") idList: String,
        @Query("key") key: String,
        @Query("token") token: String
    ): Completable

    //Обновление доски
    @PUT("1/cards/{id}")
    fun updateCard(
        @Path("id") cardId: String,
        @Query("pos") pos: String,
        @Query("idList") idList: String,
        @Query("key") key: String,
        @Query("token") token: String
    ): Completable

    @DELETE("1/boards/{id}")
    fun removeBoard(
        @Path("id") boardId: String,
        @Query("key") key: String,
        @Query("token") token: String
    ): Completable

    @GET("1/cards/{id}")
    fun getCardDetails(
        @Path("id") cardId: String,
        @Query("key") key: String,
        @Query("token") token: String,
        @Query("fields") fields: String,
        @Query("board") board: Boolean,
        @Query("board_fields") board_fields: String,
        @Query("list") list: Boolean,
        @Query("list_fields") list_fields: String,
        @Query("members") members: Boolean,
        @Query("members_fields") members_fields: String
    ): Single<SpecificCard>

    //Получаю участников доски
    @GET("1/boards/{id}/members")
    fun getBoardMembers(
        @Path("id") boardId: String,
        @Query("key") key: String,
        @Query("token") token: String,
        @Query("fields") fields: String
    ): Single<List<Member>>
}
