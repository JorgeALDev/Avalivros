package com.jorge.avalivros.data.api

import com.jorge.avalivros.data.model.RespostaApi
import com.jorge.avalivros.data.model.WorkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApi {
    @GET("search.json")
    suspend fun buscarLivros(@Query("q") query: String): RespostaApi

    @GET("works/{workKey}.json")
    suspend fun buscarWork(@Path("workKey") workKey: String): WorkResponse
}