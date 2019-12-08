package com.ace.homework2.model.members

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface CardMembersApiInterface {

    @POST("1/cards/{cardId}/idMembers")
    fun addCardMember(
        @Path("cardId") cardId: String,
        @Query("value") value: String
    ): Completable

    @DELETE("1/cards/{cardId}/idMembers/{idMember}")
    fun removeCardMember(
        @Path("cardId") cardId: String,
        @Path("idMember") idMember: String
    ): Completable

    @GET("1/boards/{boardId}/members")
    fun getBoardMembers(
        @Path("boardId") boardId: String,
        @Query("fields") fields: String
    ): Single<List<Member>>
}
