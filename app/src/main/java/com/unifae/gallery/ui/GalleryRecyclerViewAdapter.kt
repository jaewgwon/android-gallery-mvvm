package com.unifae.gallery.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.unifae.gallery.R
import com.unifae.gallery.data.entity.Photo

class GalleryRecyclerViewAdapter(private val context: Context)
        : RecyclerView.Adapter<GalleryRecyclerViewAdapter.PhotoItemHolder>() {
    private val photoList = ArrayList<Photo>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItemHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_photo_list, parent, false)
        return PhotoItemHolder(view)
    }

    override fun getItemCount() = photoList.size

    override fun onBindViewHolder(holder: PhotoItemHolder, position: Int) {
        holder.bind(photoList[position])
    }

    fun setList(photoList: List<Photo>) {
        this.photoList.addAll(photoList)
    }

    fun clearList() {
        this.photoList.clear()
    }

    inner class PhotoItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(photo: Photo) {
            // photographer name
            itemView.findViewById<TextView>(R.id.item_tv_photographer).text = photo.photographer

            // load photo with glide
            GlideHelper.loadImage(
                itemView,
                itemView.findViewById(R.id.item_iv_thumbnail),
                Uri.parse(photo.thumbnailPath),
                itemView.findViewById(R.id.item_pb_loading)
            )

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