package com.jorge.avalivros.data.repository

import com.jorge.avalivros.data.api.RetrofitInstance
import com.jorge.avalivros.data.model.Livro

class LivroRepository {

    suspend fun buscarLivros(textoBusca: String): List<Livro> {
        return try {
            RetrofitInstance.api.buscarLivros(textoBusca).docs
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun buscarDetalhesPorKey(workKey: String, livroBase: Livro? = null): Livro {
        val keyLimpa = workKey.removePrefix("/works/")
        val workResponse = RetrofitInstance.api.buscarWork(keyLimpa)

        val descricao = when (val desc = workResponse.description) {
            is String -> desc
            is Map<*, *> -> desc["value"] as? String
            else -> null
        }

        val base = livroBase ?: Livro(title = workResponse.title ?: "Título não disponível")
        return base.copy(description = descricao)
    }
}