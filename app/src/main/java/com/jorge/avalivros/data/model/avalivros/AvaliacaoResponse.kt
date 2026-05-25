package com.jorge.avalivros.data.model.avalivros

data class AvaliacaoResponse(
    val id: String,
    val livro_id: String,
    val nota: Int,
    val comentario: String
)