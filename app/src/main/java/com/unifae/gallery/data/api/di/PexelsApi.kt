package com.unifae.gallery.data.api.di

import com.unifae.gallery.data.api.dto.PexelsSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsApi {
    @GET("search")
    suspend fun search(
        @Query("query") query: String,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?
    ): PexelsSearchResponse
}