package com.jorge.avalivros.data.api

import LoginResponse
import RegisterRequest
import com.jorge.avalivros.data.model.avalivros.AvaliacaoRequest
import com.jorge.avalivros.data.model.avalivros.AvaliacaoResponse
import com.jorge.avalivros.data.model.usuario.LoginRequest
import com.jorge.avalivros.data.model.usuario.Usuario
import retrofit2.http.*

interface AvalivrosApi {
    @POST("avaliacoes")
    suspend fun enviarAvaliacao(@Body request: AvaliacaoRequest): AvaliacaoResponse

    @GET("livros/{id}/avaliacoes")
    suspend fun listarAvaliacoes(@Path("id") livroId: String): List<AvaliacaoResponse>

    @POST("usuarios")
    suspend fun register(@Body request: RegisterRequest): Usuario

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: String): Usuario
}