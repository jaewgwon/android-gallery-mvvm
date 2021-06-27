package com.unifae.gallery.data.api.dto

import com.google.gson.annotations.SerializedName

data class PexelsPhotoData(
    val id: Int,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    @SerializedName("photographer_url")
    val photographerUrl: String,
    @SerializedName("photographer_id")
    val photographerId: Int,
    @SerializedName("avg_color")
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
