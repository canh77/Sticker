package com.lutech.stickerwhatsapp.api

import com.lutech.stickerwhatsapp.Utils.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
     var stickerApi : StickerApi

    init {
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        stickerApi = retrofit.create(StickerApi::class.java)
    }

    companion object{
        private var instance: RetrofitClient? = null
        @Synchronized
        fun getInstance() : RetrofitClient {
            if (instance == null){
                instance == RetrofitClient()
            }
            return instance!!
        }
    }
}