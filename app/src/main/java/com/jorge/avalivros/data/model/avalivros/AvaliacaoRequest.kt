package com.jorge.avalivros.data.model.avalivros

data class AvaliacaoRequest(
    val livroId: String,
    val avaliacao: Int,
    val comentario: String
)