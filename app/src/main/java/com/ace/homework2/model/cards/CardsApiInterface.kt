package com.ace.homework2.model.cards

import com.ace.homework2.model.boards.Board
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface CardsApiInterface {

    @GET("1/boards/{id}")
    fun getBoardDetails(
        @Path("id") boardId: String,
        @Query("cards") cards: String,
        @Query("card_fields") cardFields: String,
        @Query("lists") lists: String,
        @Query("members") members: String,
        @Query("list_fields") listFields: String,
        @Query("card_attachments") cardAttachments: Boolean,
        @Query("card_attachment_fields") cardAttachmentFields: String,
        @Query("card_members") card_members: Boolean,
        @Query("card_member_fields") card_member_fields: String
    ): Single<Board>

    @POST("1/cards")
    fun createCard(
        @Query("name") name: String,
        @Query("idList") idList: String
    ): Completable

    @PUT("1/cards/{id}")
    fun updateCard(
        @Path("id") cardId: String,
        @Query("pos") pos: String,
        @Query("idList") idList: String
    ): Completable
}