package com.unifae.gallery.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.unifae.gallery.data.api.di.PexelsApi
import com.unifae.gallery.data.api.dto.PagedPhotos
import com.unifae.gallery.data.entity.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotoDataSource @Inject constructor(private val api: PexelsApi) {

    private val _networkStatus = MutableLiveData<NetworkStatus>()
    val networkStatus: LiveData<NetworkStatus>
        get() = _networkStatus

    private val _searchResponse = MutableLiveData<PagedPhotos>()
    val searchResponse: LiveData<PagedPhotos>
        get() = _searchResponse

    suspend fun fetchPhotos(query: String, page: Int?, perPage: Int?) =
        withContext(Dispatchers.IO) {
            _networkStatus.postValue(NetworkStatus.LOADING)
            try {
                val search = async {
                    api.search(query, page, perPage)
                }
                var nextPage: String? = search.await().nextPage
                if (nextPage == null) nextPage = ""
                _searchResponse.postValue(
                    PagedPhotos(
                        nextPage,
                        search.await().photos.map {
                            Photo(
                                it.id,
                                it.src.portrait,
                                it.src.tiny,
                                it.photographer,
                                it.photographerUrl
                            )
                        }
                    )
                )
                _networkStatus.postValue(NetworkStatus.LOADING_COMPLETE)
            } catch(error: Exception) {
                Log.e("ERROR FROM PhotoDataSource", error.message.toString())
                _networkStatus.postValue(NetworkStatus.ERROR)
            }
        }
}