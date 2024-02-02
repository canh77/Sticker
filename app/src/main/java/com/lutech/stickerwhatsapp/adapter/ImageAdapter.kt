package com.lutech.stickerwhatsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lutech.stickerwhatsapp.R
import kotlinx.android.synthetic.main.item_sticker_details.view.*

class ImageAdapter(private val listImageUrls: Array<String>?) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sticker_details, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = listImageUrls?.get(position)
        holder.itemView.tvNumberImage.text = "" + (position + 1)

        Glide.with(holder.itemView)
            .load(imageUrl)
            .placeholder(R.drawable.loading_gif)
            .error(R.drawable.sticker_1)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return listImageUrls?.size ?: 0
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_Sticker_Details)
    }
}