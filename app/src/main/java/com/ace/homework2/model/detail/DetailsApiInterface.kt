package com.ace.homework2.model.detail

import com.ace.homework2.model.cards.Card
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailsApiInterface {

    /**
     * Получаю детали карточки
     */

    @GET("1/cards/{id}")
    fun getCardDetails(
        @Path("id") cardId: String,
        @Query("fields") fields: String,
        @Query("board") board: Boolean,
        @Query("board_fields") board_fields: String,
        @Query("list") list: Boolean,
        @Query("list_fields") list_fields: String,
        @Query("members") members: Boolean,
        @Query("members_fields") members_fields: String,
        @Query("attachments") attachments: Boolean
    ): Single<Card>
}