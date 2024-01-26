package com.lutech.stickerwhatsapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lutech.stickerwhatsapp.fragment.CommunityFragment
import com.lutech.stickerwhatsapp.fragment.MyStickerFragment

class MainViewPageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyStickerFragment()
            1 -> CommunityFragment()
            else -> MyStickerFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}