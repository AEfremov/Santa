package com.efremov.santa.data.server

import okhttp3.Interceptor
import okhttp3.Response

class HeadersInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("X-App-Version", "8.16")
            .addHeader("X-Device-Os", "Android")
            .build()
        return chain.proceed(request)
    }
}