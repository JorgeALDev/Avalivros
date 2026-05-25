package com.jorge.avalivros.data.api

import com.jorge.avalivros.data.model.avalivros.AvaliacaoRequest
import com.jorge.avalivros.data.model.avalivros.AvaliacaoResponse
import retrofit2.http.*

interface AvalivrosApi {
    @POST("avaliacoes")
    suspend fun enviarAvaliacao(@Body request: AvaliacaoRequest): AvaliacaoResponse

    @GET("livros/{id}/avaliacoes")
    suspend fun listarAvaliacoes(@Path("id") livroId: String): List<AvaliacaoResponse>
}