package com.lutech.stickerwhatsapp.activity

import android.app.Dialog
import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.lutech.stickerwhatsapp.BuildConfig
import com.lutech.stickerwhatsapp.R
import com.lutech.stickerwhatsapp.adapter.ImageAdapter
import com.lutech.stickerwhatsapp.contentprovider.WhitelistCheck
import com.lutech.stickerwhatsapp.model.Sticker
import kotlinx.android.synthetic.main.activity_show_data.*
import kotlinx.android.synthetic.main.toolbar_details_sticker.*


class ShowDataActivity : BaseActivity() {
    val EXTRA_STICKER_TITLE = "sticker_title"
    val EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority"
    val EXTRA_STICKER_DESCRIPTION = "sticker_description"
    private var sticker: List<Sticker> ?= null
    private val ADD_PACK = 200
    private val TAG = "ShowDataActivityTAG"


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_data)

        initData()
        handlerEvents()
        sticker = ArrayList()
    }

    private fun initData() {
        val stickerTitle = intent.getStringExtra("sticker_title")
        val stickerDescription = intent.getStringExtra("sticker_description")
        val stickerTrayIcon = intent.getStringExtra("sticker_tray_icon")
        val stickerDownloadCounter = intent.getIntExtra("sticker_download_counter", 0)
        val listImageUrls = intent.getStringArrayExtra("list_image_urls")
        tvTitleStickerDetails.text = stickerTitle
        tvStickerDescriptionDetails.text = stickerDescription
        tvNumberDownloadDetails.text = stickerDownloadCounter.toString()

        Glide.with(this)
            .load(stickerTrayIcon)
            .placeholder(R.drawable.loading_gif)
            .error(R.drawable.tray_large)
            .into(iv_sticker_tray_details)


        val imageAdapter = ImageAdapter(listImageUrls)
        rvShowDataSticker.adapter = imageAdapter

        rvShowDataSticker.layoutManager = GridLayoutManager(this, 3)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun handlerEvents() {
        ivBack.setOnClickListener {
            finish()
        }

        addToWhatsapp.setOnClickListener {
            addStickerPackToWhatsApp("abc", sticker!! )
        }


        lnShareSticker.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND_MULTIPLE
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Stickers")
            val imageUris = ArrayList<Uri>()
            val listImageUrls = intent.getStringArrayExtra("list_image_urls")
            listImageUrls?.forEach { imageUrl ->
                if (imageUrl != null && imageUrl.isNotEmpty()) {
                    val imageUri = Uri.parse(imageUrl)
                    imageUris.add(imageUri)
                }
            }
            sendIntent.type = "image/*"
            sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
            try {
                startActivity(Intent.createChooser(sendIntent, "Chia sẻ với"))
            } catch (e: Exception) {
                Log.e("ShowDataActivity", "Không thể chia sẻ ảnh", e)
                Toast.makeText(this, "Không thể chia sẻ ảnh", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addStickerPackToWhatsApp(title: String?, stickerPackName: List<Sticker>) {
        //not add sticker to whatsapp
//        if (!WhitelistCheck.isWhatsAppConsumerAppInstalled(packageManager) && !WhitelistCheck.isWhatsAppSmbAppInstalled(packageManager)) {
//
//            Toast.makeText(this, "add pack fail prompt update whatsapp", Toast.LENGTH_LONG)
//                .show()
//            return
//        }
//        try {
//            launchIntentToAddPackToChooser(title!!, stickerPackName!!)
//            Toast.makeText(this, "đã dô addStickerPackToWhatsApp", Toast.LENGTH_LONG).show()
//        } catch (e: java.lang.Exception) {
//            Toast.makeText(this, "đã dô catch", Toast.LENGTH_LONG).show()
//        }


           try {
             var stickerPackWhitelistedInWhatsAppConsumer = WhitelistCheck.isStickerPackWhitelistedInWhatsAppConsumer(this, getTitle().toString())
                var stickerPackWhitelistedInWhatsAppSmb = WhitelistCheck.isStickerPackWhitelistedInWhatsAppSmb(this,  getTitle().toString())
            if (!stickerPackWhitelistedInWhatsAppConsumer && !stickerPackWhitelistedInWhatsAppSmb) {
                launchIntentToAddPackToChooser( getTitle().toString(), stickerPackName)
            } else if (!stickerPackWhitelistedInWhatsAppConsumer) {
                launchIntentToAddPackToSpecificPackage(getTitle().toString(),  stickerPackName, WhitelistCheck.CONSUMER_WHATSAPP_PACKAGE_NAME)
            } else if (!stickerPackWhitelistedInWhatsAppSmb) {
                launchIntentToAddPackToSpecificPackage(getTitle().toString(), stickerPackName, WhitelistCheck.SMB_WHATSAPP_PACKAGE_NAME)
            } else {
                Toast.makeText(this,"add launchIntentToAddPackToChooser to whatsapp", Toast.LENGTH_LONG).show()
                launchIntentToAddPackToChooser(getTitle().toString(),  stickerPackName)

            }
        } catch (e : Exception) {
               Log.e(TAG, "error adding sticker pack to WhatsApp", e);
           }
    }


    private fun launchIntentToAddPackToSpecificPackage(
        title: String,
        listSticker: List<Sticker>,
        whatsappPackageName: String,
    ) {
        val intent = createIntentToAddStickerPack(title, listSticker)
        intent.setPackage(whatsappPackageName)
        try {
            startActivityForResult(intent, ADD_PACK)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "add_pack_fail_prompt_update_whatsapp", Toast.LENGTH_LONG).show()
        }
    }

    //Handle cases either of WhatsApp are set as default app to handle this intent. We still want users to see both options.
    private fun launchIntentToAddPackToChooser(title: String, listSticker: List<Sticker>) {
        val intent = createIntentToAddStickerPack(title, listSticker)
        Log.d("11120201010121", "createIntentToAddStickerPack: "+"title " + getTitle() + "listSticker " +listSticker)
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.app_name)), ADD_PACK)
            Toast.makeText(this, "Đã vào launchIntentToAddPackToChooser", Toast.LENGTH_LONG).show()

        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "đã vào catch", Toast.LENGTH_LONG).show()

        }
    }

    private fun createIntentToAddStickerPack(title: String, listSticker: List<Sticker>): Intent {
        val intent = Intent()
        intent.action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
        intent.putExtra(EXTRA_STICKER_TITLE, title)
        intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY)
        intent.putExtra(EXTRA_STICKER_DESCRIPTION, listSticker.size)
        return intent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_PACK) {
            if (resultCode == RESULT_CANCELED) {
                if (data != null) {
                    val validationError = data.getStringExtra("validation_error")
                    if (validationError != null) {
                        if (BuildConfig.DEBUG) {
                            //validation error should be shown to developer only, not users.
                            MessageDialogFragment.newInstance(R.string.txt_error_add_sticker, validationError).show(supportFragmentManager, "validation error")
                        }
                        Log.e(TAG, "Validation failed:$validationError")
                    }
                } else {
                   ShowDataActivity.StickerPackNotAddedMessageFragment().show(supportFragmentManager, "sticker_pack_not_added")
                }
            }
        }
    }

    class StickerPackNotAddedMessageFragment : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(
                requireActivity())
                .setMessage(R.string.hello_blank_fragment)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok,
                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int -> dismiss() })
                .setNeutralButton(R.string.hello_blank_fragment) { dialog, which -> launchWhatsAppPlayStorePage() }
            return dialogBuilder.create()
        }

        private fun launchWhatsAppPlayStorePage() {
            if (activity != null) {
                val packageManager = requireActivity().packageManager
//                val whatsAppInstalled =
//                    activity.addToWhatsapp.isPackageInstalled(WhitelistCheck.CONSUMER_WHATSAPP_PACKAGE_NAME, packageManager)
//                val smbAppInstalled = WhitelistCheck.isPackageInstalled(WhitelistCheck.SMB_WHATSAPP_PACKAGE_NAME, packageManager)
//                val playPackageLinkPrefix = "http://play.google.com/store/apps/details?id="
//                if (whatsAppInstalled && smbAppInstalled) {
//                    launchPlayStoreWithUri("https://play.google.com/store/apps/developer?id=WhatsApp+LLC")
//                } else if (whatsAppInstalled) {
//                    launchPlayStoreWithUri(playPackageLinkPrefix + WhitelistCheck.CONSUMER_WHATSAPP_PACKAGE_NAME)
//                } else if (smbAppInstalled) {
//                    launchPlayStoreWithUri(playPackageLinkPrefix + WhitelistCheck.SMB_WHATSAPP_PACKAGE_NAME)
//                }
            }
        }

        private fun launchPlayStoreWithUri(uriString: String) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(uriString)
            intent.setPackage("com.android.vending")
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(activity, com.google.android.material.R.string.error_a11y_label, Toast.LENGTH_LONG).show()
            }
        }
    }
}