package com.unifae.gallery.data.api.dto

data class PexelsSearchResponse(
    val totalResults: Int,
    val page: Int,
    val perPage: Int,
    val Photos: List<PexelsPhotoData>,
    val nextPage: String
)