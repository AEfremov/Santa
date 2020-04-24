package com.efremov.santa.data.server

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    val authToken = "Basic NTczZDVmMGItOGNmZC00MjRmLTk3YmUtNzIzNzlkZmU5MDExOmU0ODI5ZjQ3LTg5YzktNDc0Ny1hNDg5LWYwZjgyNzZkNWQwNQ==".trim()

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("Authorization", authToken)
            .build()
        return chain.proceed(request)
    }
}