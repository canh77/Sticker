package com.lutech.stickerwhatsapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.lutech.stickerwhatsapp.R
import com.lutech.stickerwhatsapp.adapter.StickerAdapter
import com.lutech.stickerwhatsapp.model.Sticker
import kotlinx.android.synthetic.main.fragment_community.*

class CommunityFragment : Fragment() {

    private var stickerAdapter : StickerAdapter ?= null
    private val stickerPacks: ArrayList<Sticker> = ArrayList<Sticker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?, ): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stickerAdapter = StickerAdapter(requireContext(), stickerPacks)
        rvSticker.adapter = stickerAdapter

        swipeRefreshLayout.setOnRefreshListener {
            stickerAdapter?.notifyDataSetChanged()
            Toast.makeText(requireContext(), "Đã load data", Toast.LENGTH_SHORT).show()
            swipeRefreshLayout.isRefreshing = false
        }
    }


}