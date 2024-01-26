package com.lutech.stickerwhatsapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lutech.stickerwhatsapp.R
import com.lutech.stickerwhatsapp.api.RetrofitClient
import com.lutech.stickerwhatsapp.model.Sticker
import retrofit2.Call
import retrofit2.Retrofit

class LoadDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_data)
        loadSticker()
    }

    private fun loadSticker(){
//        if (stickerDao.getSticker().isEmpty()){
//            val call: Call<Sticker> = RetrofitClient.getInstance().stickerApi.getDataSticker()
//        }
    }
}