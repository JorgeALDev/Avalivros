package com.jorge.avalivros.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AvalivrosRetrofitInstance {
    private const val BASE_URL = "https://avalivro-api-13.onrender.com/"

    val api: AvalivrosApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AvalivrosApi::class.java)
    }
}