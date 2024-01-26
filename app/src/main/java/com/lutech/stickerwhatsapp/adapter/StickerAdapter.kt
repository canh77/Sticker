package com.lutech.stickerwhatsapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lutech.stickerwhatsapp.R
import com.lutech.stickerwhatsapp.model.Sticker
import kotlinx.android.synthetic.main.list_item_sticker.view.*


class StickerAdapter(
    var context: Context, private var listSticker: ArrayList<Sticker>,
) : RecyclerView.Adapter<StickerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(context).inflate(R.layout.list_item_sticker, parent, false)
        return MyViewHolder(myView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemView = holder.itemView
        val stickerPack = listSticker[position]

        itemView.tvTitleSticker.text = stickerPack.title
        itemView.tvTitleDetailSticker.text = stickerPack.title
        itemView.tvNumberDownload.text = stickerPack.download_counter.toString()


        Glide.with(context)
            .load(stickerPack.tray_icon)
            .into(itemView.iv_sticker_tray)

        Glide.with(context)
            .load(stickerPack.images)
            .into(itemView.sticker_one)
        Log.d("11114444411", "stickerPack.images: $stickerPack.images")

        itemView.setOnClickListener {

        }


    }


    override fun getItemCount(): Int {
        return listSticker.size
    }


}