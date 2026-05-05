package com.jorge.avalivros.data.model

data class RespostaApi(val docs: List<Livro>)

data class Livro(
    val title: String,
    val author_name: List<String>? = null,
    val first_publish_year: Int? = null,
    val cover_i: Int? = null,
    val key: String? = null,
    val description: String? = null
)