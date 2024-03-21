package com.lutech.stickerwhatsapp.activity

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.lutech.stickerwhatsapp.R
import com.lutech.stickerwhatsapp.utils.Utils
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.dialog_info_sticker.*
import kotlinx.android.synthetic.main.dialog_login_facebook_gg.*
import kotlinx.android.synthetic.main.dialog_login_facebook_gg.btnClose
import kotlinx.android.synthetic.main.dialog_login_facebook_gg.btnRegister
import kotlinx.android.synthetic.main.dialog_login_facebook_gg.tvServiceAndPrivacy
import kotlinx.android.synthetic.main.dialog_register_facebook_gg.*


class SettingActivity : AppCompatActivity() {
    private var mDialogLogin: Dialog? = null
    private var mDialogRegister: Dialog? = null
    var mAuth = FirebaseAuth.getInstance()


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initData()
        handlerEvents()
    }

    private fun initData() {

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun handlerEvents() {
        btnBackSettings.setOnClickListener {
            finish()
        }
        lnLoginRegistration.setOnClickListener {
            dialogLogin()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun dialogLogin() {
        mDialogLogin = Utils.onCreateDialog(this, R.layout.dialog_login_facebook_gg, true)
        mDialogLogin?.btnClose?.setOnClickListener {
            mDialogLogin?.dismiss()
        }

        mDialogLogin?.btnRegister?.setOnClickListener {
            dialogRegister()
            mDialogLogin?.dismiss()
        }

        val html =
            "<a href='https://storage.lutech.vn/app/sticker/meme_pack1/sticker_11.png'><b>Terms of Service</b></a> " +
                    "and <a href='https://storage.lutech.vn/app/sticker/meme_pack1/sticker_11.png'><b>Privacy Policy</b></a>."
        val styledText = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)

        mDialogLogin?.tvServiceAndPrivacy?.text = styledText
        mDialogLogin?.tvServiceAndPrivacy?.movementMethod =
            LinkMovementMethod.getInstance()
        mDialogLogin?.show()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun dialogRegister() {
        mDialogRegister =
            Utils.onCreateDialog(this, R.layout.dialog_register_facebook_gg, true)
        mDialogRegister?.btnClose?.setOnClickListener {
            mDialogRegister?.dismiss()
        }

        var email : String = mDialogRegister?.edtEmailRegister?.text.toString().trim()

        mDialogRegister?.btnRegister?.setOnClickListener {
            Log.d("11011100022", "email: "+email)
//            mAuth?.fetchSignInMethodsForEmail(email)
//                ?.addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val result = task.result
//                        if (result.signInMethods!!.size == 0) {
//                            Toast.makeText(this, "Địa chỉ email chưa được sử dụng", Toast.LENGTH_SHORT).show()
//                            // Địa chỉ email chưa được sử dụng
//                            // Thực hiện các hành động phù hợp ở đây
//                        } else {
//                            Toast.makeText(this, "Địa chỉ email đã tồn tại", Toast.LENGTH_SHORT).show()
//                            // Địa chỉ email đã tồn tại
//                            // Thông báo cho người dùng biết và xử lý tùy ý
//                        }
//                    } else {
//                        // Xử lý lỗi nếu cần
//                        Toast.makeText(this, "else", Toast.LENGTH_SHORT).show()
//                    }
//                }
        }




        val html = "<a href='https://storage.lutech.vn/app/sticker/meme_pack1/sticker_11.png'><b>Terms of Service</b></a> " +
                    "and <a href='https://storage.lutech.vn/app/sticker/meme_pack1/sticker_11.png'><b>Privacy Policy</b></a>."
        val styledText = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)

        mDialogRegister?.tvServiceAndPrivacy?.text = styledText
        mDialogRegister?.tvServiceAndPrivacy?.movementMethod =
            LinkMovementMethod.getInstance()
        mDialogRegister?.show()
    }
}