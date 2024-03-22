package com.lutech.stickerwhatsapp.activity

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lutech.stickerwhatsapp.R
import com.lutech.stickerwhatsapp.adapter.MainViewPageAdapter
import com.lutech.stickerwhatsapp.utils.Constants
import com.lutech.stickerwhatsapp.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_info_sticker.*
import kotlinx.android.synthetic.main.dialog_login_facebook_gg.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var mDialogInfSticker: Dialog? = null
    @RequiresApi(Build.VERSION_CODES.N)
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

    @RequiresApi(Build.VERSION_CODES.N)
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun dialogInfSticker() {
        mDialogInfSticker = Utils.onCreateDialog(this, R.layout.dialog_info_sticker, true)

        //Privacy policy
        val html = "<a href='https://storage.lutech.vn/app/sticker/meme_pack1/sticker_11.png'>Privacy policy</a> "
        val styledText = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
        mDialogInfSticker?.btnPrivacy?.text = styledText
        mDialogInfSticker?.btnPrivacy?.movementMethod =
            LinkMovementMethod.getInstance()

        //Terms of service
        val htmlPolicy = "<a href='https://storage.lutech.vn/app/sticker/meme_pack1/sticker_11.png'>Terms of service</a> "
        val styledTexts = Html.fromHtml(htmlPolicy, Html.FROM_HTML_MODE_COMPACT)
        mDialogInfSticker?.btnService?.text = styledTexts
        mDialogInfSticker?.btnService?.movementMethod =
            LinkMovementMethod.getInstance()


        mDialogInfSticker?.show()
    }


}