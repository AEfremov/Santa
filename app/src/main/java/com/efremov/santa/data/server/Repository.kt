package com.efremov.santa.data.server

import com.efremov.santa.data.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class Repository {

    companion object {
        const val LOG_TAG = "Repository"
        const val MAX_CONNECTION_TIMEOUT = 90000L
        const val MAX_READ_TIMEOUT = 90000L
        const val MAX_WRITE_TIMEOUT = 90000L
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(HeadersInterceptor())
        .addInterceptor(AuthInterceptor())
        .connectTimeout(MAX_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(MAX_READ_TIMEOUT, TimeUnit.MILLISECONDS)
        .writeTimeout(MAX_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
        .build()

    private val api: Api = Retrofit.Builder().baseUrl("http://secret-santa.l-tech.ru/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(client)
        .build()
        .create(Api::class.java)

    fun signUp(body: SignUpParams) : Deferred<Response<UserResponse>> {
        return api.signUp(body)
    }

    fun joinGroup(body: JoinGroupParams) : Deferred<Response<GroupResponse>> {
        return api.joinGroup(body)
    }

    fun obtainGroups(userId: String) : Deferred<Response<GroupDataResponse>> {
        return api.obtainGroups(userId)
    }

    fun createGroup(body: CreateGroupParams) : Deferred<Response<GroupResponse>> {
        return api.createGroup(body)
    }

    fun getGroupById(userId: String, groupId: String) : Deferred<Response<GroupResponse>> {
        return api.getGroupById(groupId, userId)
    }

    fun drawSanta(body: SantaDrawParams) : Deferred<Response<GroupResponse>> {
        return api.drawSanta(body)
    }

    fun showSanta(groupId: String, userId: String) : Deferred<Response<UserResponse>> {
        return api.showSanta(groupId, userId)
    }
}