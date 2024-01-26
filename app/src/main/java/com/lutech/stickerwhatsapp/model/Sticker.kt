package com.lutech.stickerwhatsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "sticker")
data class Sticker(
        val title: String,
        val description: String?,
        val tray_icon: String,
        val download_counter: Int,
        val staff_pick: Int,
        val facebook_url: String,
        val instagram_url: String?,
        val twitter_url: String?,
        val tiktok_url: String?,
        val images: List<StickerImage>
    )


