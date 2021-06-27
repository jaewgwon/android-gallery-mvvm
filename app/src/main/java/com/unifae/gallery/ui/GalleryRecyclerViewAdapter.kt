package com.unifae.gallery.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.unifae.gallery.R
import com.unifae.gallery.data.entity.Photo

class GalleryRecyclerViewAdapter(private val context: Context, private val photoList: List<Photo>)
        : RecyclerView.Adapter<GalleryRecyclerViewAdapter.PhotoItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItemHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_photo_list, parent, false)
        return PhotoItemHolder(view)
    }

    override fun getItemCount() = photoList.size

    override fun onBindViewHolder(holder: PhotoItemHolder, position: Int) {
        holder.bind(photoList[position])
    }

    inner class PhotoItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(photo: Photo) {
            // photographer name
            itemView.findViewById<TextView>(R.id.item_tv_photographer).text = photo.photographer

            // load photo with glide
            Glide.with(itemView)
                .load(Uri.parse(photo.thumbnailPath))
                .listener(object: RequestListener<Drawable> {
                    val progressBar =
                        itemView.findViewById<ProgressBar>(R.id.item_pb_loading)
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                })
                .skipMemoryCache(true) // TODO(): do or not
                .into(itemView.findViewById(R.id.item_iv_thumbnail))

            itemView.setOnClickListener {
                val intent = Intent(context, PhotoDetailActivity::class.java)
                intent.putExtra("photographer", photo.photographer)
                intent.putExtra("url", photo.photoPath)
                intent.putExtra("photographerUrl", photo.photographerUrl)
                startActivity(context, intent, Bundle())
            }
        }
    }
}