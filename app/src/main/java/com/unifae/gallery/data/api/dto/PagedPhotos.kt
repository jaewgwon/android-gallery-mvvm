package com.unifae.gallery.data.api.dto

import com.unifae.gallery.data.entity.Photo

data class PagedPhotos(
    val nextPage: String,
    val photos: List<Photo>
)