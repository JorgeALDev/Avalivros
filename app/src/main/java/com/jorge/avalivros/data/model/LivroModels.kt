package com.jorge.avalivros.data.model

data class RespostaApi(
    val docs: List<Livro>
)

data class Livro(
    val title: String,
    val author_name: List<String>?,
    val first_publish_year: Int?,
    val cover_i: Int?
)