package com.unifae.gallery.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unifae.gallery.data.api.dto.PagedPhotos
import com.unifae.gallery.data.repository.NetworkStatus
import com.unifae.gallery.data.repository.PhotoDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(private val repository: PhotoDataSource): ViewModel()  {
    val photoList: LiveData<PagedPhotos> = repository.searchResponse
    val networkStatus: LiveData<NetworkStatus> = repository.networkStatus

    fun getPhotosByQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchPhotos(query, null, null)
        }
    }

    fun getNextPage(nextPageUrl: String) {
        val uri = Uri.parse(nextPageUrl)
        val query = uri.getQueryParameter("query")!!
        val nextPage = uri.getQueryParameter("page")!!.toInt()
        val perPage = uri.getQueryParameter("per_page")!!.toInt()
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchPhotos(query, nextPage, perPage)
        }
    }
}