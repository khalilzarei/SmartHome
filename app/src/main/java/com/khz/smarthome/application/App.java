package com.khz.smarthome.application;

import android.app.Application;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.snackbar.Snackbar;
import com.khz.smarthome.R;
import com.khz.smarthome.helper.SessionManager;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


public class App extends Application {

    public static  String         TAG = App.class.getSimpleName();
    private static App            instance;
    public static  SessionManager session;

    @Override
    public void onCreate() {
        super.onCreate();
        session = new SessionManager(this);
        if (SessionManager.isNightModeOn()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

//        TypefaceUtil.overrideFont(this, "fonts/" + SessionManager.getFont() + ".TTF");

        instance = this;
        ViewPump.init(
                ViewPump.builder()
                        .addInterceptor(
                                new CalligraphyInterceptor(
                                        new CalligraphyConfig.Builder()
                                                .setDefaultFontPath("fonts/" + SessionManager.getFont() + ".TTF")
                                                .setFontAttrId(R.attr.fontPath)
                                                .build()
                                )
                        ).build());
    }

    public int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    public int pxToDp(int px) {
        return (int) (px / getResources().getDisplayMetrics().density);
    }

    public static boolean isEmailValid(String email) {
        String  expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern    = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher    = pattern.matcher(email);
        return matcher.matches();
    }


    public static int getHeightSize() {
        DisplayMetrics displayMetrics = instance.getResources().getDisplayMetrics();
        float          dpHeight       = displayMetrics.heightPixels / displayMetrics.density;
        return Math.round(dpHeight * 1.9f);
    }

    public static int getWidthSize() {
        DisplayMetrics displayMetrics = instance.getResources().getDisplayMetrics();
        float          dpWidth        = (displayMetrics.widthPixels) / displayMetrics.density;
        return Math.round(dpWidth * 1.9f);
    }

    public static void showErrorSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setBackgroundTint(Color.RED)
                .setTextColor(Color.YELLOW).show();
    }

    public static boolean isNetworkConnected() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static App getInstance() {
        return instance;
    }

    public static void showSuccessSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setBackgroundTint(Color.GREEN)
                .setTextColor(Color.BLACK).show();
    }
}