package com.ace.homework2.model.boards

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BoardApiInterface {

    /**
     * Получение списка досок
     */

    @GET("1/members/me/boards")
    fun getBoards(
        @Query("organization") organization: Boolean,
        @Query("fields") fields: String): Single<MutableList<Board>>

    /**
     * Создание доски
     */

    @POST("1/boards/")
    fun createBoard(
        @Query("name") name: String,
        @Query("idOrganization") idOrganization: String,
        @Query("defaultLists") defaultLists: Boolean): Single<Board>
}

