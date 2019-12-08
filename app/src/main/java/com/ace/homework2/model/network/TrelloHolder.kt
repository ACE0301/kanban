package com.ace.homework2.model.network

import com.github.scribejava.apis.TrelloApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1RequestToken
import com.github.scribejava.core.oauth.OAuth10aService
import com.github.scribejava.httpclient.okhttp.OkHttpHttpClientConfig
import java.util.concurrent.Future

object TrelloHolder {
    const val REST_URL = "https://api.trello.com/"
    const val REST_CONSUMER_KEY = "e00f6e3314ffeb372dbeb1ec8c613aa6"
    const val REST_CONSUMER_SECRET =
        "86d2d96f9f606dda52bf3fd87c2d39636786fb4e27d4a2cad14d57351c59b2ed"
    const val REST_CALLBACK_URL = "https://androidtfs"

    var service: OAuth10aService = ServiceBuilder(REST_CONSUMER_KEY)
        .apiSecret(REST_CONSUMER_SECRET)
        .callback(REST_CALLBACK_URL)
        .httpClientConfig(OkHttpHttpClientConfig.defaultConfig())
        .build(TrelloApi.instance())
    var requestToken: Future<OAuth1RequestToken> = service.requestTokenAsync
    var authUrl: String = service.getAuthorizationUrl(requestToken.get())
    var url = "$authUrl&scope=read,write"
}
