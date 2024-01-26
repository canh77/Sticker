package com.lutech.stickerwhatsapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lutech.stickerwhatsapp.R
import com.lutech.stickerwhatsapp.adapter.MainViewPageAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_coin.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        handlerEvents()
    }

    private fun initData() {
        fragmentContainer.offscreenPageLimit = 2
        val mAdapter = MainViewPageAdapter(this)
        fragmentContainer!!.adapter = mAdapter
        fragmentContainer!!.isUserInputEnabled = false
    }

    private fun handlerEvents() {

        bottomNavigation!!.setOnItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.btnMySticker) {
                bottomNavigation!!.menu.findItem(R.id.btnMySticker).isChecked = true
                fragmentContainer!!.setCurrentItem(0, false)
            }else if (item.itemId == R.id.bottCommunitys){
                bottomNavigation!!.menu.findItem(R.id.bottCommunitys).isChecked = true
                fragmentContainer!!.setCurrentItem(1, false)
            }
            false
        })


        toolbar_back.setOnClickListener {
            Log.d("110100000100", "setOnClickListener: ")
//            ApiCall().getJokes(this) { sticker ->
//                toolbar_title.text = sticker.value
//                Log.d("110100000100", "jokes.title: "+sticker.value)
//            }

            val intent = Intent(this,LoadDataActivity::class.java)
            startActivity(intent)
        }
    }
}