package com.unifae.gallery.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.unifae.gallery.data.api.PexelsClient
import com.unifae.gallery.data.repository.NetworkStatus
import com.unifae.gallery.databinding.ActivityGalleryBinding

class GalleryActivity : AppCompatActivity() {
    private lateinit var layout: ActivityGalleryBinding
    private var currentStatus: NetworkStatus = NetworkStatus.LOADING_COMPLETE

    private val viewModel: GalleryViewModel by viewModels {
        GalleryViewModelFactory(PexelsClient.getClient())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityGalleryBinding.inflate(layoutInflater)
        layout.galleryRvList.layoutManager = LinearLayoutManager(this)
        setContentView(layout.root)
    }

    override fun onResume() {
        super.onResume()
        // network status observer
        viewModel.getDataSourceNetworkStatus().observe(
            this,
            Observer {
                currentStatus = it
                onNetworkStatusChanged(it)
            }
        )

        // button event handler
        layout.galleryBtnSearch.setOnClickListener { onSearchButtonClicked(it) }
    }

    private fun onSearchButtonClicked(view: View) {
        hideKeyboard(view)
        if (currentStatus != NetworkStatus.LOADING) {
            viewModel
                .searchPhotos(layout.galleryEtSearch.text.toString())
                .observe(
                    this,
                    Observer { photoList ->
                        layout.galleryRvList.adapter = GalleryRecyclerViewAdapter(this, photoList)
                    }
                )
        }
    }

    private fun onNetworkStatusChanged(networkStatus: NetworkStatus) {
        if (networkStatus == NetworkStatus.LOADING) layout.galleryWrapperLoading.visibility = View.VISIBLE
        else if (networkStatus == NetworkStatus.LOADING_COMPLETE) layout.galleryWrapperLoading.visibility = View.GONE
    }

    fun hideKeyboard(view: View) {
        val imm: InputMethodManager? = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}