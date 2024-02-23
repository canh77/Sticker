package com.lutech.stickerwhatsapp.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.lutech.stickerwhatsapp.BuildConfig
import com.lutech.stickerwhatsapp.R
import com.lutech.stickerwhatsapp.adapter.ImageAdapter
import com.lutech.stickerwhatsapp.model.Sticker1
import com.lutech.stickerwhatsapp.model.StickerImage
import kotlinx.android.synthetic.main.activity_show_data.*
import kotlinx.android.synthetic.main.toolbar_details_sticker.*
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


class ShowDataActivity : AddStickerPackActivity() {
//class ShowDataActivity : BaseActivity() {
    val EXTRA_STICKER_PACK_ID = "sticker_pack_id"
    val EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority"
    val EXTRA_STICKER_PACK_NAME = "sticker_pack_name"
    val EXTRA_STICKER_PACK_LIST_DATA = "sticker_pack_list"


    private var sticker: List<StickerImage>? = null
    private val ADD_PACK = 200
    private var savedBitmap: Bitmap? = null
    private val sticker1: Sticker1? = null
    private var mImageUrl : String ?= null
    private var mImageUrlList: MutableList<String> = mutableListOf()


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


//        addToWhatsapp.setOnClickListener {
//            val stickerTitle = intent.getStringExtra("sticker_title")
//            val listImageUrls = intent.getStringArrayExtra("list_image_urls")
//            listImageUrls?.forEach { imageUrl ->
//                if (imageUrl != null && imageUrl.isNotEmpty()) {
//                    val imageUri = Uri.parse(imageUrl)
//                    mImageUrl = imageUri.toString()
//                    Log.d("012345678998745", "imageUri.toString():  " + imageUri.toString())
//                }
//            }
//            addStickerPackToWhatsApp(stickerTitle.toString(), mImageUrl.toString())
//            Log.d("012345678998745", "Add : stickerTitle " + stickerTitle + "    mImageUrl " + mImageUrl)
//        }


        //save media storage
//            var mImage: Bitmap?
//            val stickerTrayIcon = intent.getStringExtra("sticker_tray_icon")
//            Log.d("01234567899874", "stickerTrayIcon: " + stickerTrayIcon.toString())
//            val myExecutor = Executors.newSingleThreadExecutor()
//            val myHandler = Handler(Looper.getMainLooper())
//            myExecutor.execute {
//                mImage = mLoad(stickerTrayIcon.toString())
//                myHandler.post {
//                    iv_sticker_tray_details.setImageBitmap(mImage)
//                    if (mImage != null) {
//                        mSaveMediaToStorage(mImage)
//                    }
//                }
//            }



        addToWhatsapp.setOnClickListener {
            // Thêm các URL của hình ảnh vào danh sách
            val imageUrls = listOf(
                "https://storage.lutech.vn/app/sticker/meme_pack_1/sticker_5.png",
//                "https://storage.lutech.vn/app/sticker/meme_pack_1/sticker_9.png"
            )
            // Tải và lưu hình ảnh từ các URL vào bộ nhớ của thiết bị
            DownloadImagesTask(this).execute(*imageUrls.toTypedArray())
            Log.d("01234567899874511", "DownloadImagesTask: execute "+imageUrls.toTypedArray().toString())

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
                startActivity(Intent.createChooser(sendIntent, "Share with..."))
            } catch (e: Exception) {
                Log.e("01234567899874", "Không thể chia sẻ ảnh", e)
                Toast.makeText(this, "Không thể chia sẻ ảnh", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private inner class DownloadImagesTask(private val context: Context) : AsyncTask<String, Void, List<Uri>>() {
        override fun doInBackground(vararg urls: String): List<Uri> {
            val imageUris = mutableListOf<Uri>()
            Log.d("0123456789987451", "imageUris: " +imageUris)
            for (url in urls) {
                val bitmap = BitmapFactory.decodeStream(URL(url).openStream())
                Log.d("0123456789987451", "doInBackground: url  "+url)
                val imageUri = saveBitmapToInternalStorage(bitmap)
                imageUris.add(imageUri)
                Log.d("0123456789987451", "imageUri: " +imageUri)
            }
            return imageUris
        }

        override fun onPostExecute(result: List<Uri>) {
            super.onPostExecute(result)
            for (imageUri in result) {
                addImageToWhatsAppStickers(imageUri,title.toString()   )
            }
        }

        //save bitmap
        private fun saveBitmapToInternalStorage(bitmap: Bitmap): Uri {
            val folder = File(context.filesDir, "WhatsAppStickers")
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val imageFile = File(folder, "${System.currentTimeMillis()}.png")
            Log.d("012345678998745", "imageFile: " +imageFile)
            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
            return Uri.fromFile(imageFile)
        }
    }

    //add sticker
    private fun addImageToWhatsAppStickers(imageUri: Uri , title : String) {
        val intent = Intent()
        intent.action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
        intent.putExtra("sticker_pack_id", "title")
        intent.putExtra("sticker_pack_authority", "${packageName}.contentProvider.StickerContentProvider2")
        intent.putExtra("sticker_pack_name", "description")
        intent.putExtra("sticker_pack_publisher", "download_counter")
        intent.putExtra("sticker_pack_tray_icon", "sticker_tray_icon")
        intent.putExtra("sticker_pack_publisher_email", "your_email@example.com")
        intent.putExtra("sticker_pack_publisher_website", "https://yourwebsite.com")
        intent.putExtra("sticker_pack_privacy_policy_website", "https://yourwebsite.com/privacy_policy.html")
        intent.putExtra("sticker_pack_license_agreement_website", "https://yourwebsite.com/license_agreement.html")
        intent.putExtra("sticker_pack_icon", "your_sticker_pack_icon_uri")
        intent.putExtra("sticker_pack_thumbnail", imageUri)
        Log.d("012345678998745", "addImageToWhatsAppStickers: imageUri "+imageUri)
        startActivity(intent)
    }


    private fun addStickerToWhatsApp(bitmap: Bitmap?) {
        if (bitmap == null) {
            Toast.makeText(this, "Bitmap is null", Toast.LENGTH_SHORT).show()
            return
        }
        // Sau khi đã chuyển đổi và lưu sticker, thêm vào WhatsApp
        val stickersIntent = Intent()
        stickersIntent.action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
        stickersIntent.putExtra("sticker_pack_id", "title")
        stickersIntent.putExtra("sticker_pack_authority", BuildConfig.CONTENT_PROVIDER_AUTHORITY)
        stickersIntent.putExtra("sticker_pack_name", "description")
        stickersIntent.putExtra("sticker_pack_publisher", "tray_icon")
        Log.d("012345678998745", "addStickerToWhatsApp: title " +title)
        try {
            startActivity(stickersIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to add sticker to WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }
    // Function to establish connection and load image
    private fun mLoad(string: String): Bitmap? {
        val url: URL = mStringToURL(string)!!
        Log.d("012345678998745", "mLoad: url "+url)
        val connection: HttpURLConnection?
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
    // Function to convert string to URL
    private fun mStringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }
    private fun mSaveMediaToStorage(bitmap: Bitmap?) {
        val filename = "${System.currentTimeMillis()}.webp"
//        val filename = "${System.currentTimeMillis()}.png"
        Log.d("012345678998745", "mSaveMediaToStorage: filename " + filename)
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/webp")
//                    put(MediaStore.MediaColumns.MIME_TYPE,"image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.WEBP, 100, it)
            Toast.makeText(this, "Saved to Gallery", Toast.LENGTH_SHORT).show()
        }
        savedBitmap = bitmap
        addStickerToWhatsApp(savedBitmap)
        Log.d("01234567899874", "savedBitmap: " + savedBitmap)
    }


//    private fun addStickerPackToWhatsApp(title: String?, description: String) {
//        val stickerDescription = intent.getStringExtra("sticker_description")
//        launchIntentToAddPackToChooser(title.toString(), stickerDescription.toString())
//        Log.d("01234567899874",
//            "title " + title.toString() + "  description " + stickerDescription.toString())
//
//    }


    private fun launchIntentToAddPackToChoosers(title: String, stickerPackName: String) {
        val intent = createIntentToAddStickerPack(title, stickerPackName)
        Log.d("01234567899874", "launchIntentToAddPackToChoosers: title " + title + "description " + title)
        try {
            startActivityForResult(Intent.createChooser(intent, "Add to Whatsapp 1"), ADD_PACK)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "catch vào add to whatsapp 1", Toast.LENGTH_LONG).show()
        }
    }
    private fun createIntentToAddStickerPack(title: String, stickerPackName: String): Intent {
        val intent = Intent()
        intent.action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
        intent.putExtra(EXTRA_STICKER_PACK_ID, title)
        Log.d("01234567899874", "identifier: $title")
        intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY)
        intent.putExtra(EXTRA_STICKER_PACK_NAME, stickerPackName)
        Log.d("01234567899874", "description: $stickerPackName")
        return intent
    }


    private fun launchIntentToAddPackToSpecificPackage(
        title: String,
        description: String,
        whatsappPackageName: String,
    ) {
        val intent = createIntentToAddStickerPack(title, description)
        intent.setPackage(whatsappPackageName)
        try {
            startActivityForResult(intent, ADD_PACK)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this,
                " catch vào launchIntentToAddPackToSpecificPackage",
                Toast.LENGTH_LONG).show()
        }
    }

    //Handle cases either of WhatsApp are set as default app to handle this intent. We still want users to see both options.
    private fun launchIntentToAddPackToChooser(title: String, description: String) {
        val intent = createIntentToAddStickerPack(title, description)
        Log.d("11120201010121",
            "createIntentToAddStickerPack: " + "title " + getTitle() + "description " + description)
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.app_name)),
                ADD_PACK)
            Toast.makeText(this, "Đã vào launchIntentToAddPackToChooser", Toast.LENGTH_LONG).show()
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "catch vào launchIntentToAddPackToChooser", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_PACK) {
            if (resultCode == RESULT_CANCELED) {
                if (data != null) {
                    val validationError = data.getStringExtra("validation_error")
                    Log.d("11232115541", "validationError: " +validationError)
                    if (validationError != null) {
//                        if (BuildConfig.DEBUG) {
//                            MessageDialogFragment.newInstance(R.string.txt_error_add_sticker, validationError).show(supportFragmentManager, "validation error")
//                        }
                        Log.d("11232115541", "$validationError: $validationError")
                    }
                } else {
                    Toast.makeText(this, "đã click ok", Toast.LENGTH_SHORT).show()
                    StickerPackNotAddedMessageFragment().show(supportFragmentManager, "sticker_pack_not_added")
                }
            }
        }
    }

    class StickerPackNotAddedMessageFragment : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                .setMessage("Error")
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok,
                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int -> dismiss() })
                .setNeutralButton("") { dialog, which -> launchWhatsAppPlayStorePage() }
            return dialogBuilder.create()
        }

        private fun launchWhatsAppPlayStorePage() {
            if (activity != null) {

            }
        }
    }

//    override fun addStickerPackToWhatsApp(identifier: String, stickerPackName: String) {
//        try {
//            val stickerPackWhitelistedInWhatsAppConsumer =
//                WhitelistCheck.isStickerPackWhitelistedInWhatsAppConsumer(this, identifier)
//            val stickerPackWhitelistedInWhatsAppSmb =
//                WhitelistCheck.isStickerPackWhitelistedInWhatsAppSmb(this, identifier)
//            if (!stickerPackWhitelistedInWhatsAppConsumer && !stickerPackWhitelistedInWhatsAppSmb) {
//                //ask users which app to add the pack to.
//                launchIntentToAddPackToChooser(identifier, stickerPackName)
//            } else if (!stickerPackWhitelistedInWhatsAppConsumer) {
//                launchIntentToAddPackToSpecificPackage(identifier,
//                    stickerPackName,
//                    WhitelistCheck.CONSUMER_WHATSAPP_PACKAGE_NAME)
//            } else if (!stickerPackWhitelistedInWhatsAppSmb) {
//                launchIntentToAddPackToSpecificPackage(identifier,
//                    stickerPackName,
//                    WhitelistCheck.SMB_WHATSAPP_PACKAGE_NAME)
//            } else {
//                launchIntentToAddPackToChooser(identifier, stickerPackName)
//                Log.d("012345678998745",
//                    "launchIntentToAddPackToChooser $identifier stickerPackName $stickerPackName")
//            }
//        } catch (e: java.lang.Exception) {
//            Log.e(TAG, "error adding sticker pack to WhatsApp", e)
//            Toast.makeText(this, "R.string.2", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    private fun launchIntentToAddPackToSpecificPackage(
//        identifier: String,
//        stickerPackName: String,
//        whatsappPackageName: String,
//    ) {
//        val intent: Intent = createIntentToAddStickerPack(identifier, stickerPackName)
//        intent.setPackage(whatsappPackageName)
//        try {
//            startActivityForResult(intent, ADD_PACK)
//        } catch (e: ActivityNotFoundException) {
//            Toast.makeText(this,
//                " R.string.add_pack_fail_prompt_update_whatsapp",
//                Toast.LENGTH_LONG).show()
//        }
//    }
//
//    //Handle cases either of WhatsApp are set as default app to handle this intent. We still want users to see both options.
//    private fun launchIntentToAddPackToChooser(identifier: String, stickerPackName: String) {
//        val intent: Intent = createIntentToAddStickerPack(identifier, stickerPackName)
//        try {
//            startActivityForResult(Intent.createChooser(intent, "Add sticker to whatsapp"), ADD_PACK)
//        } catch (e: ActivityNotFoundException) {
//            Toast.makeText(this, "R.string.add_pack_fail_prompt_update_whatsapp", Toast.LENGTH_LONG)
//                .show()
//        }
//    }
//
//    private fun createIntentToAddStickerPack(identifier: String, stickerPackName: String): Intent {
//        val intent = Intent()
//        intent.action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
//        intent.putExtra(AddStickerPackActivity.EXTRA_STICKER_PACK_ID, identifier)
//        intent.putExtra(AddStickerPackActivity.EXTRA_STICKER_PACK_AUTHORITY,
//            BuildConfig.CONTENT_PROVIDER_AUTHORITY)
//        intent.putExtra(AddStickerPackActivity.EXTRA_STICKER_PACK_NAME, stickerPackName)
//        Log.d("01234567899874",
//            "createIntentToAddStickerPack:  name  $stickerPackName    identifier $identifier")
//        return intent
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode ==ADD_PACK) {
//            Log.d("01234567899874",
//                "requestCode: " + requestCode + "ADD_PACK " + ADD_PACK)
//            if (resultCode == RESULT_CANCELED) {
//                if (data != null) {
//                    val validationError = data.getStringExtra("validation_error")
//                    if (validationError != null) {
////                        if (BuildConfig.DEBUG) {
////                            //validation error should be shown to developer only, not users.
////                            MessageDialogFragment.newInstance(R.string.txt_error_add_sticker, validationError).show(getSupportFragmentManager(), "validation error");
////                        }
//                        Log.e(TAG,
//                            "Validation failed:$validationError")
//                    }
//                } else {
//                    AddStickerPackActivity.StickerPackNotAddedMessageFragment().show(
//                        supportFragmentManager, "sticker_pack_not_added")
//                }
//            }
//        }
//    }
}