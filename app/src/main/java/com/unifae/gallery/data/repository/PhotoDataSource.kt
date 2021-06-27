package com.unifae.gallery.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.unifae.gallery.data.api.PexelsAPI
import com.unifae.gallery.data.entity.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class PhotoDataSource(private val api: PexelsAPI) {

    private val _networkStatus = MutableLiveData<NetworkStatus>()
    val networkStatus: LiveData<NetworkStatus>
        get() = _networkStatus

    private val _searchResponse = MutableLiveData<List<Photo>>()
    val searchResponse: LiveData<List<Photo>>
        get() = _searchResponse

    suspend fun fetchPhotos(query: String) =
        withContext(Dispatchers.IO) {
            _networkStatus.postValue(NetworkStatus.LOADING)
            try {
                val search = async {
                    api.get(query)
                }
                _searchResponse.postValue(
                    search.await().photos.map {
                        Photo(
                            it.id,
                            it.src.original,
                            it.src.small,
                            it.photographer,
                            it.photographerUrl
                        )
                    }
                )
                _networkStatus.postValue(NetworkStatus.LOADING_COMPLETE)
            } catch(error: Exception) {
                Log.e("ERROR FROM PhotoDataSource", error.message.toString())
                _networkStatus.postValue(NetworkStatus.ERROR)
            }
        }
}