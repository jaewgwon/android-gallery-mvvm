package com.unifae.gallery.ui

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.unifae.gallery.data.api.PexelsClient
import com.unifae.gallery.databinding.ActivityPhotoDetailBinding

class PhotoDetailActivity: AppCompatActivity() {
    private lateinit var layout: ActivityPhotoDetailBinding
    private val viewModel: GalleryViewModel by viewModels {
        GalleryViewModelFactory(PexelsClient.getClient())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityPhotoDetailBinding.inflate(layoutInflater)
        Log.d("PhotoDetail", "created")
        if (
            intent.hasExtra("photographer")
            && intent.hasExtra("url")
            && intent.hasExtra("photographerUrl")
        ) {
            val photographer = intent.getStringExtra("photographer")
            val url = intent.getStringExtra("url")
            val photographerUrl = intent.getStringExtra("photographerUrl")
            layout.photoTvPhotographer.text = photographer
            layout.photoTvPhotographerUrl.text = photographerUrl
            Glide.with(layout.root)
                .load(Uri.parse(url))
                .fitCenter()
                .listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        layout.photoPbLoading.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        layout.photoPbLoading.visibility = View.GONE
                        return false
                    }
                })
                .into(layout.photoIvImage)
        } else Log.e("PhotoDetailActivity", "No bundles")
        setContentView(layout.root)
    }
    override fun onResume() {
        super.onResume()

    }
}