package com.unifae.gallery.data.api

import com.unifae.gallery.data.api.dto.PexelsSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsAPI {
    @GET("search")
    suspend fun get(@Query("query") query: String): PexelsSearchResponse
}