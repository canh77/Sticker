package com.lutech.stickerwhatsapp.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.lutech.stickerwhatsapp.R;

import java.io.File;

public class Utilss {
    private static Dialog dialog;

    public static Dialog onCreateDialog(Context context, int id, boolean isCancelOnTouchOutside) {
        dialog = new Dialog(context);
        dialog.setContentView(id);
        dialog.setCanceledOnTouchOutside(isCancelOnTouchOutside);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        return dialog;
    }

    public static File fileBitmap;

    public static void createFolder(Context context) {
        File file = fileBitmap;
        if (file == null || !file.exists()) {
            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (externalStoragePublicDirectory == null) {
                externalStoragePublicDirectory = Environment.getExternalStorageDirectory();
            }
            fileBitmap = new File(externalStoragePublicDirectory, Constant.APP_NAME);
            if (!fileBitmap.exists()) {
                fileBitmap.mkdirs();
            }
        }
    }



    public static void changeStatusBarColor(Activity activity)
    {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.purple_700));
        }
    }

    public static void saveSharedPreferences(String KEY, String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY, value);
        editor.apply();
    }

    public static String getSharedPreference(String KEY, Context context) {
        SharedPreferences prfs = context.getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        return prfs.getString(KEY, "0");
    }


}
