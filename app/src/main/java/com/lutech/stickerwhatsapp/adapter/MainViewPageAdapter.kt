package com.lutech.stickerwhatsapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lutech.stickerwhatsapp.fragment.CommunityFragment

class MainViewPageAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CommunityFragment()
            1 -> CommunityFragment()
            else -> CommunityFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}