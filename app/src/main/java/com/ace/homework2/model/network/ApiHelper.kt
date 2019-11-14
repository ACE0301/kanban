package com.ace.homework2.model.network

import com.ace.homework2.model.network.TrelloHolder.REST_URL
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiHelper {

    private val retrofit = Retrofit.Builder()
        .baseUrl(REST_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.newThread()))
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(ApiInterface::class.java)
}