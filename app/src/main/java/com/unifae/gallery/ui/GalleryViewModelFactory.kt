package com.unifae.gallery.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unifae.gallery.data.api.PexelsAPI

class GalleryViewModelFactory(private val api: PexelsAPI): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = GalleryViewModel(api) as T
}