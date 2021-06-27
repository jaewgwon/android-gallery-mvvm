package com.unifae.gallery.ui

import android.os.Bundle
import android.util.Log
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
            }
        )

        // button event handler
        layout.galleryBtnSearch.setOnClickListener { onSearchButtonClicked() }
    }

    private fun onSearchButtonClicked() {
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
}