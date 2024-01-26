package com.lutech.stickerwhatsapp.model

import androidx.room.PrimaryKey
data class StickerImage(
//    @PrimaryKey(autoGenerate = true)
    val sticker_index: Int,
    val url: String,
    val is_animated: Int,
)