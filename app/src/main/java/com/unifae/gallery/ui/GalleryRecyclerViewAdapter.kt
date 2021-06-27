package com.unifae.gallery.ui

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
                .placeholder(R.drawable.ic_baseline_not_interested_24)
                .skipMemoryCache(true) // TODO(): do or not
                .into(itemView.findViewById(R.id.item_iv_thumbnail))
        }
    }
}