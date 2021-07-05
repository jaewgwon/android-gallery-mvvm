package com.unifae.gallery.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.unifae.gallery.databinding.ActivityPhotoDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoDetailActivity: AppCompatActivity() {
    private lateinit var layout: ActivityPhotoDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityPhotoDetailBinding.inflate(layoutInflater)

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

            GlideHelper.loadImage(
                layout.root,
                layout.photoIvImage,
                Uri.parse(url),
                layout.photoPbLoading
            )
        } else Log.e("PhotoDetailActivity", "No extras")
        setContentView(layout.root)
        layout.photoBtnBack.setOnClickListener { onBackPressed() }
    }
}