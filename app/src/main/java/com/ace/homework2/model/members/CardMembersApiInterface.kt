package com.ace.homework2.model.members

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface CardMembersApiInterface {

    /**
     * Добавление участника к карточке
     */

    @POST("1/cards/{id}/idMembers")
    fun addCardMember(
        @Path("id") cardId: String,
        @Query("value") value: String,
        @Query("key") key: String,
        @Query("token") token: String
    ): Completable

    /**
     * Удаление участника из карточки
     */

    @DELETE("1/cards/{id}/idMembers/{idMember}")
    fun removeCardMember(
        @Path("id") cardId: String,
        @Path("idMember") idMember: String,
        @Query("key") key: String,
        @Query("token") token: String
    ): Completable


    /**
     * Получаю участников доски
     */

    @GET("1/boards/{id}/members")
    fun getBoardMembers(
        @Path("id") boardId: String,
        @Query("key") key: String,
        @Query("token") token: String,
        @Query("fields") fields: String
    ): Single<List<Member>>
}
