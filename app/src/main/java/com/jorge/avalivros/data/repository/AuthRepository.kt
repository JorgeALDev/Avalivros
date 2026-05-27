package com.jorge.avalivros.data.repository

import LoginResponse
import RegisterRequest
import android.util.Log
import com.jorge.avalivros.data.api.AvalivrosRetrofitInstance
import com.jorge.avalivros.data.model.usuario.*

class AuthRepository {
    private val api = AvalivrosRetrofitInstance.api

    suspend fun register(email: String, senha: String): Usuario? {
        return try {
            val request = RegisterRequest(email, senha)
            val response = api.register(request)
            response
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun login(email: String, senha: String): LoginResponse? {
        return try {
            val request = LoginRequest(email, senha)
            api.login(request)
        } catch (e: Exception) {
            null
        }
    }
}