package com.lutech.stickerwhatsapp.utils

import android.app.*
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.lutech.stickerwhatsapp.R

import java.util.*

object Utils {
    var onShow = false
    var contentFeedback = ""

    fun setIsRating(context: Context) {
        val sharePef = context.getSharedPreferences(Constants.App_Name, Activity.MODE_PRIVATE)
        val editor = sharePef.edit()
        editor.putBoolean(Constants.KEY_IS_RATING, true)
        editor.apply()
    }

     fun feedBack(context: Context) {
        val uriText = "mailto:${Constants.email_feedback}" + "?subject="+ Uri.encode("Feedback Phone Call Screen Theme 3D App") +
                "&body=" + Uri.encode(getSystemInfo(context) + contentFeedback)
        val uri = Uri.parse(uriText)
        val sendIntent = Intent(Intent.ACTION_SENDTO)
        sendIntent.data = uri
        context.startActivity(Intent.createChooser(sendIntent, "Send email"))

    }

     fun getSystemInfo(context: Context): String {
        return try {
            val packageManager = context.packageManager
            val versionCode = packageManager.getPackageInfo(context.packageName, 0).versionCode
            val deviceModel = Build.MODEL // Lấy dòng máy của thiết bị
            val androidVersion = Build.VERSION.RELEASE // Lấy phiên bản Android của thiết bị
            val defaultLanguage = Locale.getDefault().displayLanguage // Lấy ngôn ngữ mặc định của thiết bị
            "Version App: $versionCode\n Device model: $deviceModel\nAndroid version: $androidVersion\nDefault language: $defaultLanguage\nContent: "
        } catch (e: Exception) {
            ""
        }


    }

     fun shareApp(context: Context) {
        ShareCompat.IntentBuilder.from(context as Activity).setType("text/plain").setChooserTitle("Chooser title")
            .setText(context.getString(R.string.app_name) +"  \n"+ "http://play.google.com/store/apps/details?id=" + context.packageName).startChooser()
    }

     fun policy(context: Context) {
        try {
            val url = context.getString(R.string.link_privacy_policy)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun goToCHPlay(context: Context) {
        val appPackageName: String =
            context.packageName // getPackageName() from Context or Activity object
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    fun isRating(context: Context): Boolean {
        val sharePef = context.getSharedPreferences(Constants.App_Name, Activity.MODE_PRIVATE)
        return sharePef.getBoolean(Constants.KEY_IS_RATING, false)
    }

    fun setTrackEvent(context: Context, key: String, value: String) {
        val params = Bundle()
        params.putString(key, value)
        FirebaseAnalytics.getInstance(context).logEvent("$key", params)
    }


    fun isGetLanguage(context: Context): Boolean {
        val sharePef = context.getSharedPreferences(Constants.App_Name, Activity.MODE_PRIVATE)
        return sharePef.getBoolean(Constants.IS_SET_LANG, false)
    }

    fun isSetLanguage(context: Context) {
        val sharePef = context.getSharedPreferences(Constants.App_Name, Activity.MODE_PRIVATE)
        val editor = sharePef.edit()
        editor.putBoolean(Constants.IS_SET_LANG, true)
        editor.apply()
    }

    fun getIOSCountryData(context: Context): String {
        val sharePef = context.getSharedPreferences(Constants.App_Name, Activity.MODE_PRIVATE)
        return sharePef.getString(Constants.KEY_LANG, "en").toString()
    }

    fun setIOSCountryData(lang: String, context: Context) {
        val sharePef = context.getSharedPreferences(Constants.App_Name, Activity.MODE_PRIVATE)
        val editor = sharePef.edit()
        editor.putString(Constants.KEY_LANG, lang)
        editor.apply()
    }

    fun getCurrentFlag(context: Context): Int {
        val sharePef = context.getSharedPreferences(Constants.App_Name, Activity.MODE_PRIVATE)
        return sharePef.getInt(Constants.KEY_FLAG, 0)
    }

    fun setCurrentFlag(flag: Int, context: Context) {
        val sharePef = context.getSharedPreferences(Constants.App_Name, Activity.MODE_PRIVATE)
        val editor = sharePef.edit()
        editor.putInt(Constants.KEY_FLAG, flag)
        editor.apply()
    }

    fun setLanguageForApp(context: Context) {
        val languageToLoad = getIOSCountryData(context)
        val locale: Locale = if (languageToLoad == "not-set") {
            Locale.getDefault()
        } else {
            Locale(languageToLoad)
        }
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )
    }

    fun onCreateDialog(
        applicationContext: Context?,
        dialog_update_version: Int,
        isCanceledOnTouchOutside: Boolean = true
    ): Dialog {
        val dialogRate = Dialog(applicationContext!!)
        dialogRate.setContentView(dialog_update_version)
        dialogRate.setCanceledOnTouchOutside(isCanceledOnTouchOutside)
        dialogRate.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialogRate.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialogRate
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            ?.getState() === NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    ?.getState() === NetworkInfo.State.CONNECTED
    }

    fun changeStatusBarColor(activity: Activity, color: Int) {
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity, color)
    }

    fun onCreateProgressDialog(context: Context, msg: Int): Dialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.setIndeterminate(true)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage(context.getString(msg))
        return progressDialog
    }
}