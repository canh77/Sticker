package com.lutech.stickerwhatsapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lutech.stickerwhatsapp.Utils.Constant;
import com.lutech.stickerwhatsapp.model.Sticker1;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl(Constant.INSTANCE.getBASE_URL())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("data_sticker_2.json")
    Call<List<Sticker1>> getListSticker(@Query("title") String title);

}
