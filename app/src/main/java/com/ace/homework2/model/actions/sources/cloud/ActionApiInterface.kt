package com.ace.homework2.model.actions.sources.cloud

import com.ace.homework2.model.actions.data.Action
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ActionApiInterface {

    @GET("1/cards/{id}/actions")
    fun getActions(
        @Path("id") cardId: String,
        @Query("filter") filter: String
    ): Single<List<Action>>
}