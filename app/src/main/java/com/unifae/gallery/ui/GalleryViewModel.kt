package com.unifae.gallery.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.unifae.gallery.data.api.PexelsAPI
import com.unifae.gallery.data.entity.Photo
import com.unifae.gallery.data.repository.NetworkStatus
import com.unifae.gallery.data.repository.PhotoDataSource

class GalleryViewModel(private val api: PexelsAPI): ViewModel()  {
    lateinit var dataSource: PhotoDataSource

    suspend fun searchPhotos (query: String): LiveData<List<Photo>> {
        dataSource = PhotoDataSource(api)
        dataSource.fetchPhotos(query)
        return dataSource.searchResponse
    }

    fun getDataSourceNetworkStatus(): LiveData<NetworkStatus> {
        return dataSource.networkStatus
    }
}