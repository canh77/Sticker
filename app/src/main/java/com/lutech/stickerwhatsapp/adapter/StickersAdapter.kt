package com.lutech.stickerwhatsapp.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.lutech.stickerwhatsapp.adapter.StickersAdapter.StickerViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import com.lutech.stickerwhatsapp.R
import com.bumptech.glide.Glide
import android.widget.Toast
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.lutech.stickerwhatsapp.activity.ShowDataActivity
import android.widget.TextView
import com.lutech.stickerwhatsapp.model.Sticker

class StickersAdapter(private val mListStickers: List<Sticker>?) :
    RecyclerView.Adapter<StickerViewHolder>() {
    private val mContext: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_sticker, parent, false)
        return StickerViewHolder(view)
    }

    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
        val stickers = mListStickers!![position] ?: return
        holder.tvTitleSticker.text = stickers.title
        holder.tvNumberDownload.text = stickers.download_counter.toString()
        holder.tvTitleDetailSticker.text = stickers.description
        holder.tv_total_image.text = "+" + stickers.images.size
        Glide.with(holder.itemView)
            .load(stickers.tray_icon)
            .placeholder(R.drawable.loading_gif)
            .error(R.drawable.tray_large)
            .into(holder.iv_sticker_tray)
        val stickerImagesList = stickers.images
        var i = 0
        while (i < stickerImagesList.size && i < 10) {
            val stickerImage = stickerImagesList[i]
            val imageUrl = stickerImage.url
            var imageViewId: Int = when (i) {
                0 -> holder.sticker_one.id
                1 -> holder.sticker_two.id
                2 -> holder.sticker_three.id
                3 -> holder.sticker_four.id
                4 -> holder.sticker_five.id
                5 -> holder.sticker_six.id
                6 -> holder.sticker_sevens.id
                7 -> holder.sticker_eights.id
                else -> {
                    i++
                    continue
                }
            }
            val imageView = holder.itemView.findViewById<ImageView>(imageViewId)
            Glide.with(holder.itemView)
                .load(imageUrl)
                .placeholder(R.drawable.loading_gif)
                .error(R.drawable.sticker_1)
                .into(imageView)
            i++
        }
        holder.itemView.setOnClickListener { view ->
            val selectedSticker = mListStickers!![position]
            val intent = Intent(view.context, ShowDataActivity::class.java)
            intent.putExtra("sticker_title", selectedSticker.title)
            intent.putExtra("sticker_description", selectedSticker.description)
            intent.putExtra("sticker_tray_icon", selectedSticker.tray_icon)
            intent.putExtra("sticker_download_counter", selectedSticker.download_counter)
            // Truyền danh sách hình ảnh
            val listImageUrls = selectedSticker.images.map { it.url }.toTypedArray()
            intent.putExtra("list_image_urls", listImageUrls)
            view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mListStickers?.size ?: 0
    }

    inner class StickerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_sticker_tray: ImageView
        val sticker_two: ImageView
        val sticker_one: ImageView
        val sticker_three: ImageView
        val sticker_four: ImageView
        val sticker_five: ImageView
        val sticker_six: ImageView
        val sticker_sevens: ImageView
        val sticker_eights: ImageView
        val tvTitleDetailSticker: TextView
        val tvTitleSticker: TextView
        val tvNumberDownload: TextView
        val tv_total_image: TextView

        init {
            tvTitleSticker = itemView.findViewById(R.id.tvTitleSticker)
            tv_total_image = itemView.findViewById(R.id.tv_total_image)
            tvTitleDetailSticker = itemView.findViewById(R.id.tvTitleDetailSticker)
            tvNumberDownload = itemView.findViewById(R.id.tvNumberDownload)
            iv_sticker_tray = itemView.findViewById(R.id.iv_sticker_tray)
            sticker_two = itemView.findViewById(R.id.sticker_two)
            sticker_one = itemView.findViewById(R.id.sticker_one)
            sticker_three = itemView.findViewById(R.id.sticker_three)
            sticker_four = itemView.findViewById(R.id.sticker_four)
            sticker_five = itemView.findViewById(R.id.sticker_five)
            sticker_six = itemView.findViewById(R.id.sticker_six)
            sticker_sevens = itemView.findViewById(R.id.sticker_sevens)
            sticker_eights = itemView.findViewById(R.id.sticker_eights)
        }
    }
}