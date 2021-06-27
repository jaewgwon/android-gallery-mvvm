package com.unifae.gallery.data.api.dto

import com.google.gson.annotations.SerializedName

data class PexelsSearchResponse(
    @SerializedName("total_result")
    val totalResults: Int,
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    val photos: List<PexelsPhotoData>,
    @SerializedName("next_page")
    val nextPage: String
)