package com.ace.homework2.model.network

import com.ace.homework2.model.network.TrelloHolder.REST_URL
import com.ace.homework2.view.ui.boards.token
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiHolder {

    var okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val originalHttpUrl = original.url
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("key", TrelloHolder.REST_CONSUMER_KEY)
                    .addQueryParameter("token", token)
                    .build()
                val requestBuilder = original.newBuilder()
                    .url(url)

                val request = requestBuilder.build()
                return chain.proceed(request)
            }
        })
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(REST_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.newThread()))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}