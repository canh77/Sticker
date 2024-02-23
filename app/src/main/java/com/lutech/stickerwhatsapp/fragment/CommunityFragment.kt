package com.lutech.stickerwhatsapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lutech.stickerwhatsapp.R
import com.lutech.stickerwhatsapp.adapter.StickersAdapter
import com.lutech.stickerwhatsapp.api.ApiService
import com.lutech.stickerwhatsapp.model.Sticker1
import kotlinx.android.synthetic.main.fragment_community.*
import kotlinx.android.synthetic.main.sticker_packs_list_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityFragment : Fragment() {

    private var mListSticker: List<com.lutech.stickerwhatsapp.model.Sticker1>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?, ): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
        }
        mListSticker = ArrayList()
        val layoutManager = LinearLayoutManager(requireContext())
        rvDataStickers?.layoutManager = layoutManager
        callApiSticker()


//        showStickerPackList(stickerPackList!!)

    }

    private fun callApiSticker() {
        ApiService.apiService.getListSticker("title").enqueue(object : Callback<List<Sticker1?>> {
            override fun onResponse(
                call: Call<List<Sticker1?>>,
                response: Response<List<com.lutech.stickerwhatsapp.model.Sticker1?>>,
            ) {
                mListSticker = response.body() as List<com.lutech.stickerwhatsapp.model.Sticker1>?
                val stickerAdapter = StickersAdapter(mListSticker)
                rvDataStickers?.adapter = stickerAdapter
                Toast.makeText(requireContext(), "Load data success", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<com.lutech.stickerwhatsapp.model.Sticker1?>>, t: Throwable) {
                Toast.makeText(requireContext(), "Load data fail", Toast.LENGTH_SHORT).show()
            }
        })
    }


}