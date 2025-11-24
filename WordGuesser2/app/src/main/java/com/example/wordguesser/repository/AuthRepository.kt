package com.example.wordguesser.repository

import com.example.wordguesser.model.User
import com.example.wordguesser.network.RetrofitClient

class AuthRepository {
    private val api = RetrofitClient.api

    suspend fun login(user: User): Result<String> {
        return try {
            val response = api.login(user)
            if (response.isSuccessful) {
                val body = response.body()
                val message = body?.get("message")?.toString() ?: "Login exitoso"
                Result.success(message)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(user: User): Result<String> {
        return try {
            val response = api.register(user)
            if (response.isSuccessful) {
                Result.success("Registro exitoso")
            } else {
                Result.failure(Exception("Error al registrar: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}