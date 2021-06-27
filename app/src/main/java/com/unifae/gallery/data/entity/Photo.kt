package com.unifae.gallery.data.entity

data class Photo(
    val id: Int,
    val photoPath: String,
    val thumbnailPath: String,
    val author: String,
    val date: String,
)
