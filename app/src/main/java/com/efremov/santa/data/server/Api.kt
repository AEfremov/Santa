package com.efremov.santa.data.server

import com.efremov.santa.data.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface Api {

    companion object {
        const val API_PATH = "ru/api/v1"
    }

    @POST("$API_PATH/users")
    fun signUp(
        @Body params: SignUpParams
    ) : Deferred<Response<UserResponse>>

    @POST("$API_PATH/joins")
    fun joinGroup(
        @Body params: JoinGroupParams
    ) : Deferred<Response<GroupResponse>>

    @GET("$API_PATH/groups")
    fun obtainGroups(
        @Query("user_id") userId: String
    ) : Deferred<Response<GroupDataResponse>>

    @POST("$API_PATH/groups")
    fun createGroup(
        @Body params: CreateGroupParams
    ) : Deferred<Response<GroupResponse>>

    @GET("$API_PATH/groups/{id}")
    fun getGroupById(
        @Path("id", encoded = true) groupId: String,
        @Query("user_id") userId: String
    ) : Deferred<Response<GroupResponse>>

    @GET("$API_PATH/groups/{id}/santa")
    fun showSanta(
        @Path("id", encoded = true) groupId: String,
        @Query("user_id") userId: String
    ) : Deferred<Response<UserResponse>>

    @POST("$API_PATH/draw")
    fun drawSanta(
        @Body params: SantaDrawParams
    ) : Deferred<Response<GroupResponse>>
}