package com.unifae.gallery.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unifae.gallery.data.api.PexelsAPI
import com.unifae.gallery.data.entity.Photo
import com.unifae.gallery.data.repository.NetworkStatus
import com.unifae.gallery.data.repository.PhotoDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryViewModel(api: PexelsAPI): ViewModel()  {
    private val dataSource: PhotoDataSource = PhotoDataSource(api)

    fun searchPhotos (query: String): LiveData<List<Photo>> {
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.fetchPhotos(query)
        }
        return dataSource.searchResponse
    }

    fun getDataSourceNetworkStatus(): LiveData<NetworkStatus> {
        return dataSource.networkStatus
    }

    override fun onCleared() {
        super.onCleared()
    }
}