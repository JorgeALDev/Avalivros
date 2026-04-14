package com.jorge.avalivros.data.api

import com.jorge.avalivros.data.model.RespostaApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenLibraryApi {
    @GET("search.json")
    suspend fun buscarLivros(@Query("q") textoBusca: String): RespostaApi
}

object RetrofitInstance {
    private const val BASE_URL = "https://openlibrary.org/"

    val api: OpenLibraryApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenLibraryApi::class.java)
    }
}