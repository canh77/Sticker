package com.lutech.stickerwhatsapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.lutech.stickerwhatsapp.R
import com.lutech.stickerwhatsapp.adapter.StickersAdapter
import com.lutech.stickerwhatsapp.api.ApiService
import com.lutech.stickerwhatsapp.model.Sticker
import kotlinx.android.synthetic.main.fragment_community.*
import kotlinx.android.synthetic.main.fragment_my_sticker.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyStickerFragment : Fragment() {
    private var mListSticker: List<Sticker>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?, ): View? {
        return inflater.inflate(R.layout.fragment_my_sticker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayouts.setOnRefreshListener {
            swipeRefreshLayouts.isRefreshing = false
        }
        mListSticker = ArrayList()
        val layoutManager = LinearLayoutManager(requireContext())
        rvDataStickerss?.layoutManager = layoutManager
        callApiSticker()
    }

    private fun callApiSticker() {
        ApiService.apiService.getListSticker("title").enqueue(object : Callback<List<Sticker?>> {
            override fun onResponse(
                call: Call<List<Sticker?>>,
                response: Response<List<Sticker?>>, ) {
                mListSticker = response.body() as List<Sticker>?
                val stickerAdapter = StickersAdapter(mListSticker)
                rvDataStickerss?.adapter = stickerAdapter
                Toast.makeText(requireContext(), "Load data success", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<Sticker?>>, t: Throwable) {
                Toast.makeText(requireContext(), "Load data fail", Toast.LENGTH_SHORT).show()
            }
        })
    }

}