package com.ace.homework2.model.boards.sources.cloud

import com.ace.homework2.model.boards.data.Board
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BoardApiInterface {

    @GET("1/members/me/boards")
    fun getBoards(
        @Query("organization") organization: Boolean,
        @Query("fields") fields: String
    ): Single<MutableList<Board>>

    @POST("1/boards/")
    fun createBoard(
        @Query("name") name: String,
        @Query("idOrganization") idOrganization: String,
        @Query("defaultLists") defaultLists: Boolean
    ): Single<Board>
}

