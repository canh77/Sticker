package com.lutech.stickerwhatsapp.extension

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.Switch
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lutech.stickerwhatsapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

fun View.showSnackBar(msg: Int){
    val notice = HtmlCompat.fromHtml("${this.context.getString(msg)}<p>",HtmlCompat.FROM_HTML_MODE_LEGACY)
    Snackbar.make(this, notice, Snackbar.LENGTH_SHORT).show()
}

fun View.showSnackBar(msg: String){
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).show()
}

fun SwitchCompat.setThumbTintList(){
    val thumbStates = ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf()
        ), intArrayOf(
            ContextCompat.getColor(this.context, R.color.purple_200),
            Color.WHITE
        )
    )
    this.thumbTintList = thumbStates
}

fun SwitchCompat.setTrackTintList(){
    val trackStates = ColorStateList(
        arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()), intArrayOf(
            ContextCompat.getColor(this.context, R.color.black),
            ContextCompat.getColor(this.context, R.color.purple_200)
        )
    )
    this.trackTintList = trackStates
}

@RequiresApi(Build.VERSION_CODES.M)
fun Switch.setThumbTintList(){
    val thumbStates = ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf()
        ), intArrayOf(
            ContextCompat.getColor(this.context,R.color.black),
            Color.WHITE
        )
    )
    this.thumbTintList = thumbStates
}

@RequiresApi(Build.VERSION_CODES.M)
fun Switch.setTrackTintList(){
    val trackStates = ColorStateList(
        arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()), intArrayOf(
            ContextCompat.getColor(this.context, R.color.black),
            ContextCompat.getColor(this.context, R.color.black)
        )
    )
    this.trackTintList = trackStates
}

fun ImageView.startOpenDoorAnimation(onAnimationEnd:() -> Unit){
    val rotate = RotateAnimation(0f, 60f, Animation.RELATIVE_TO_SELF,
        0.1f, Animation.RELATIVE_TO_SELF, 0.5f)
    rotate.duration = 1000
    rotate.interpolator = LinearInterpolator()

    this.startAnimation(rotate)

    rotate.setAnimationListener(object : Animation.AnimationListener{
        override fun onAnimationStart(p0: Animation?) {

        }

        override fun onAnimationEnd(p0: Animation?) {
           onAnimationEnd.invoke()
        }

        override fun onAnimationRepeat(p0: Animation?) {

        }
    })
}


fun <R> CoroutineScope.executeAsyncTask(
    onPreExecute: () -> Unit,
    doInBackground: () -> R,
    onPostExecute: (R) -> Unit
) = launch {
    onPreExecute()
    val result = withContext(Dispatchers.Default) { // runs in background thread without blocking the Main Thread
        doInBackground()
    }
    onPostExecute(result)
}

fun String.isExistPath(): Boolean
=  File(this).exists()

inline fun <reified T> Context.readRawJson(@RawRes rawResId: Int): T {
    resources.openRawResource(rawResId).bufferedReader().use {
        return Gson().fromJson<T>(it, object: TypeToken<T>() {}.type)
    }
}