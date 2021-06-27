package com.unifae.gallery.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unifae.gallery.data.api.PexelsAPI
import com.unifae.gallery.data.api.dto.PagedPhotos
import com.unifae.gallery.data.repository.NetworkStatus
import com.unifae.gallery.data.repository.PhotoDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryViewModel(api: PexelsAPI): ViewModel()  {
    private val dataSource: PhotoDataSource = PhotoDataSource(api)
    private var nextPageUrl: String = ""

    fun searchPhotos(query: String): LiveData<PagedPhotos> {
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.fetchPhotos(query, null, null)
        }
        return dataSource.searchResponse
    }

    fun setNextPage(page: String) {
        this.nextPageUrl = page
    }

    fun fetchNextPage(): LiveData<PagedPhotos> {
        val uri = Uri.parse(nextPageUrl)
        val query = uri.getQueryParameter("query")!!
        val nextPage = uri.getQueryParameter("page")!!.toInt()
        val perPage = uri.getQueryParameter("per_page")!!.toInt()
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.fetchPhotos(query, nextPage, perPage)
        }
        return dataSource.searchResponse
    }

    fun isNextPage(): Boolean {
        return nextPageUrl != ""
    }

    fun getDataSourceNetworkStatus(): LiveData<NetworkStatus> {
        return dataSource.networkStatus
    }

    fun getDataSourcePhotos(): LiveData<PagedPhotos> {
        return dataSource.searchResponse
    }

    override fun onCleared() {
        super.onCleared()
    }
}