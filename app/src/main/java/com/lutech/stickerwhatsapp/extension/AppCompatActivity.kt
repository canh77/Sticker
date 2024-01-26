package com.lutech.stickerwhatsapp.extension

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Vibrator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lutech.stickerwhatsapp.database.dao.StickerDao
import com.lutech.stickerwhatsapp.database.db.StickerDatabase

import java.util.concurrent.Executor


//val AppCompatActivity.stickerDao: StickerDao
//    get() = StickerDatabase.getInstance(this).stickerDao()


fun AppCompatActivity.requestStoragePermission(){
    ActivityCompat.requestPermissions(
        this, arrayOf(
            getPermission13AndDefault(),//13
        ), 1
    )
}


fun getPermission13AndDefault(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
}

//fun AppCompatActivity.sendFeedback(){
//    try {
//        val intent = Intent(Intent.ACTION_SENDTO)
//        intent.data = Uri.parse("mailto:")
//        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_feedback)))
//        intent.putExtra(Intent.EXTRA_SUBJECT, String.format(getString(R.string.txt_help_to_improve_us_email_subject), getString(R.string.app_name)))
//        startActivity(Intent.createChooser(intent, "Send Feedback"))
//    } catch (ex: ActivityNotFoundException) {
//        Toast.makeText(this, getString(R.string.txt_no_mail_found), Toast.LENGTH_SHORT)
//            .show()
//    } catch (ex: Exception) {
//        ex.printStackTrace()
//    }
//}