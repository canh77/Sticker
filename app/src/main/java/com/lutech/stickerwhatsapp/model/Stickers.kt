package com.lutech.stickerwhatsapp.model


import com.google.gson.annotations.SerializedName

class Stickers(
    @field:SerializedName("title")
    var title: String,
    var description: String,
    var tray_icon: String,
    var download_counter: Int,
    var staff_pick: Int,
    var facebook_url: String,
    var instagram_url: String,
    var twitter_url: String,
    var tiktok_url: String,
    var images: List<StickerImages>,
)
