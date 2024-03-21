package com.lutech.stickerwhatsapp.activity

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lutech.stickerwhatsapp.R
import com.lutech.stickerwhatsapp.adapter.MainViewPageAdapter
import com.lutech.stickerwhatsapp.utils.Constants
import com.lutech.stickerwhatsapp.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_info_sticker.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var mDialogInfSticker: Dialog? = null
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
//                ivSearch.visibility = View.INVISIBLE
            } else if (item.itemId == R.id.bottCommunitys) {
                bottomNavigation!!.menu.findItem(R.id.bottCommunitys).isChecked = true
                fragmentContainer!!.setCurrentItem(1, false)
//                ivSearch.visibility = View.VISIBLE
            }
            false
        })

        ivInfoSticker.setOnClickListener {
            dialogInfSticker()
        }

        ivSettingSticker.setOnClickListener {
            val intentSt = Intent(this, SettingActivity::class.java)
            startActivity(intentSt)
        }

    }

    private fun dialogInfSticker() {
        mDialogInfSticker = Utils.onCreateDialog(this, R.layout.dialog_info_sticker, true)
        mDialogInfSticker?.btnPrivacy?.setOnClickListener {
            Utils.policy(this)
        }
        mDialogInfSticker?.btnService?.setOnClickListener {
            Utils.shareApp(this)
        }
        mDialogInfSticker?.show()
    }


}