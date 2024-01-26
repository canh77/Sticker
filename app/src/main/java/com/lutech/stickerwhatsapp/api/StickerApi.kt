package com.lutech.stickerwhatsapp.api

import com.lutech.stickerwhatsapp.model.Sticker
import retrofit2.Call
import retrofit2.http.GET

interface StickerApi {
    @GET("data_sticker.json")
    fun getDataSticker() : Call<Sticker>
}