package com.jorge.avalivros.data.repository

import com.jorge.avalivros.data.api.AvalivrosRetrofitInstance
import com.jorge.avalivros.data.model.avalivros.AvaliacaoRequest
import com.jorge.avalivros.data.model.avalivros.AvaliacaoResponse

class AvalivrosRepository {
    private val api = AvalivrosRetrofitInstance.api

    suspend fun enviarAvaliacao(livroKey: String, nota: Int, comentario: String): AvaliacaoResponse? {
        return try {
            val request = AvaliacaoRequest(livroId = livroKey, avaliacao = nota, comentario = comentario)
            api.enviarAvaliacao(request)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun listarAvaliacoes(livroKey: String): List<AvaliacaoResponse> {
        return try {
            api.listarAvaliacoes(livroKey)
        } catch (e: Exception) {
            emptyList()
        }
    }
}