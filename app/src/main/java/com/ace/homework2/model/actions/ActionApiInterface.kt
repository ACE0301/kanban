package com.ace.homework2.model.actions

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ActionApiInterface {

    /**
     * Получаю историю действий по карточке
     */

    @GET("1/cards/{id}/actions")
    fun getActions(
        @Path("id") cardId: String,
        @Query("key") key: String,
        @Query("token") token: String,
        @Query("filter") filter: String
    ): Single<List<Action>>
}