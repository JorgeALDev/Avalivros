package com.jorge.avalivros.data.repository

import com.jorge.avalivros.data.api.RetrofitInstance
import com.jorge.avalivros.data.model.Livro

class LivroRepository {
    suspend fun buscarLivros(textoBusca: String): List<Livro> {
        return try {
            val resposta = RetrofitInstance.api.buscarLivros(textoBusca)
            // Retorna a variável "docs" que veio da API
            resposta.docs
        } catch (e: Exception) {
            emptyList() // Se der erro, retorna lista vazia
        }
    }
}