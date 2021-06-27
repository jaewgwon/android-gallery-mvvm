package com.unifae.gallery.data.api.dto

data class PexelsPhotoData(
    val id: Int,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    val photographerUrl: String,
    val photographerId: Int,
    val avgColor: String,
    val src: PhotoUrl
) {
    data class PhotoUrl (
        val original: String,
        val large2x: String,
        val large: String,
        val medium: String,
        val small: String,
        val portrait: String,
        val landscape: String,
        val tiny: String
    )
}
