package com.jorge.avalivros.data.repository

import android.util.Log
import com.jorge.avalivros.data.api.AvalivrosRetrofitInstance
import com.jorge.avalivros.data.model.avalivros.AvaliacaoRequest
import com.jorge.avalivros.data.model.avalivros.AvaliacaoResponse

class AvalivrosRepository {
    private val api = AvalivrosRetrofitInstance.api

    suspend fun enviarAvaliacao(livroKey: String, nota: Int, comentario: String): AvaliacaoResponse? {
        return try {
            val request = AvaliacaoRequest(livroId = livroKey, avaliacao = nota, comentario = comentario)
            Log.d("AvaliacaoRequest", "Enviando: ${request}")
            val response = api.enviarAvaliacao(request)
            response
        } catch (e: retrofit2.HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("AvaliacaoRepo", "HTTP ${e.code()}: $errorBody")
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun listarAvaliacoes(livroId: String): List<AvaliacaoResponse> {
        return try {
            api.listarAvaliacoes(livroId)
        } catch (e: Exception) {
            emptyList()
        }
    }
}