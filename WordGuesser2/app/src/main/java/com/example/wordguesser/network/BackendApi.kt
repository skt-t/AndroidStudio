package com.example.wordguesser.network

import com.example.wordguesser.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BackendApi {
    @POST("api/auth/login")
    suspend fun login(@Body user: User): Response<Map<String, Any>>

    @POST("api/auth/register")
    suspend fun register(@Body user: User): Response<Map<String, Any>>
}