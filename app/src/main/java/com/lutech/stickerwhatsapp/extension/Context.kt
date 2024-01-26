package com.lutech.stickerwhatsapp.extension

import android.content.Context
import android.graphics.Bitmap
import android.hardware.fingerprint.FingerprintManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.lutech.stickerwhatsapp.BuildConfig

import java.io.File
import java.io.FileOutputStream
import java.io.IOException


fun Context.showNotice(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.showNotice(msg: Int) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.getUrlFromDrawable(res: String): String {
    return "android.resource://$packageName/drawable/$res"
}

fun Context.getLocalUriFromBitmap(bitmap: Bitmap): Uri?{
    var bmpUri: Uri? = null
    try {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),    "${System.currentTimeMillis()}.png")
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
        out.close()
        bmpUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bmpUri
}


