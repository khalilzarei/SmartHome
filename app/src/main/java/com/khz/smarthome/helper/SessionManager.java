package com.khz.smarthome.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;

import com.khz.smarthome.R;
import com.khz.smarthome.model.User;
import com.khz.smarthome.model.UserImage;

public class SessionManager {
    private static final String PREF_NAME            = "TakeItFreePref";
    private static final String KEY_IS_LOGGED_IN     = "isLoggedIn";
    private static final String KEY_ICON_SIZE        = "KEY_ICON_SIZE";
    private static final String KEY_ICON_RES_ID      = "KEY_ICON_RES_ID";
    private static final String KEY_RGBW_DEVICE_A0   = "rgbwDeviceA0";
    private static final String KEY_RGBW_MASTER_ID   = "key_rgbw_master_id";
    private static final String KEY_CLICKED          = "clicked";
    public static        String KEY_IP_STATUS        = "ipStatus";
    public static        String KEY_SOUND_PLAY       = "sound_play";
    public static        String KEY_CHANGE_FONT      = "change_font";
    public static        String KEY_VIBRATION        = "vibration";
    public static        String KEY_TOKEN            = "token";
    public static        String KEY_IP               = "ip";
    public static        String KEY_SECRET           = "secret";
    public static        String KEY_EXPIRE_TOKEN     = "expire_token";
    public static        String ID                   = "id";
    public static        String KEY_PROJECT_ID       = "project_id";
    public static        String NAME                 = "name";
    public static        String EMAIL                = "email";
    public static        String PHONE_NUMBER         = "phone_number";
    public static        String PROJECTS_COUNT       = "project_count";
    public static        String USER_IMAGE           = "user_image";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String KEY_FONT             = "default_font";
    private static final String KEY_FONT_SIZE        = "default_font_size";
    private static final String KEY_IS_NIGHT_MODE    = "isNightModeOn";

    private static String TAG = SessionManager.class.getSimpleName();
    //    private static Editor editor;

    // Shared Preferences
    private static SharedPreferences pref;
    private static Editor            editor;

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        // Shared pref mode
        int PRIVATE_MODE = 0;
        pref   = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public static void setIsLoggedIn(boolean loggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, loggedIn);
        editor.commit();
    }

    public static boolean getClicked() {
        return pref.getBoolean(KEY_CLICKED, false);
    }

    public static void setClicked(boolean clicked) {
        editor.putBoolean(KEY_CLICKED, clicked);
        editor.commit();
    }

    public static void setIpStatus(int offset) {
        editor.putInt(KEY_IP_STATUS, offset);
        editor.commit();
    }

    public static int getIpStatus() {
        return pref.getInt(KEY_IP_STATUS, View.VISIBLE);
    }

    public static void setProjectId(String projectId) {
        editor.putString(KEY_PROJECT_ID, projectId);
        editor.commit();
    }

    public static String getProjectId() {
        return pref.getString(KEY_PROJECT_ID, "");
    }


    public static void setPlaySound(boolean playSound) {
        editor.putBoolean(KEY_SOUND_PLAY, playSound);
        editor.commit();
    }

    public static boolean getPlaySound() {
        return pref.getBoolean(KEY_SOUND_PLAY, true);
    }

    public static void setChangeFont(boolean playSound) {
        editor.putBoolean(KEY_CHANGE_FONT, playSound);
        editor.commit();
    }

    public static boolean getChangeFont() {
        return pref.getBoolean(KEY_CHANGE_FONT, false);
    }

    public static void setVibration(boolean playSound) {
        editor.putBoolean(KEY_VIBRATION, playSound);
        editor.commit();
    }

    public static boolean getVibration() {
        return pref.getBoolean(KEY_VIBRATION, true);
    }


    public static void setToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public static String getToken() {
        return pref.getString(KEY_TOKEN, null);
    }


    public static void setIp(String ip) {
        editor.putString(KEY_IP, ip);
        editor.commit();
    }

    public static String getIP() {
        return pref.getString(KEY_IP, "");
    }

    public static void setSecret(String secret) {
        editor.putString(KEY_SECRET, secret);
        editor.commit();
    }

    public static String getSecret() {
        return pref.getString(KEY_SECRET, null);
    }

    public static void setExpireToken(String token) {
        editor.putString(KEY_EXPIRE_TOKEN, token);
        editor.commit();
    }

    public static String getExpireToken() {
        return pref.getString(KEY_EXPIRE_TOKEN, null);
    }


    public static void setUser(User user) {
        editor.putInt(ID, user.getId());
        editor.putString(NAME, user.getName());
        editor.putString(EMAIL, user.getEmail());
        editor.putString(PHONE_NUMBER, user.getPhoneNumber());
        editor.putInt(PROJECTS_COUNT, user.getProjectsCount());
        editor.putString(USER_IMAGE, user.getUserImage().getUrl());
        editor.commit();

    }

    public static User getUser() {
        User user = new User();
        user.setEmail(pref.getString(EMAIL, null));
        user.setId(pref.getInt(ID, 0));
        user.setName(pref.getString(NAME, null));
        user.setPhoneNumber(pref.getString(PHONE_NUMBER, null));
        user.setProjectsCount(pref.getInt(PROJECTS_COUNT, 0));
        user.setUserImage(new UserImage(pref.getString(USER_IMAGE, null)));
        return user;
    }

    public static void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public static boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public static int getFontSize() {
        return pref.getInt(KEY_FONT_SIZE, 16);
    }

    public static void setFontSize(int font) {
        editor.putInt(KEY_FONT_SIZE, font);
        editor.commit();
    }

    public static String getFont() {
        return pref.getString(KEY_FONT, "serif");
    }

    public static void setFont(String font) {
        editor.putString(KEY_FONT, font);
        editor.commit();
    }

    public static String getValue(String key) {
        return pref.getString(key, null);
    }


    public static void setIconSize(int iconSize) {
        editor.putInt(KEY_ICON_SIZE, iconSize);
        editor.commit();
    }

    public static int getIconSize() {
        return pref.getInt(KEY_ICON_SIZE, 60);
    }


    public static void setIconResID(int iconSize) {
        editor.putInt(KEY_ICON_RES_ID, iconSize);
        editor.commit();
    }

    public static int getIconResID() {
        return pref.getInt(KEY_ICON_RES_ID, R.drawable.ic_lamp_off);
    }


    public static void setValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean isNightModeOn() {
        return pref.getBoolean(KEY_IS_NIGHT_MODE, false);
    }

    public static void setIsNightModeOn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_NIGHT_MODE, isLoggedIn);
        editor.commit();
    }

//rgbwDeviceA0 = null, rgbwMasterID

    public static void setRgbwDeviceA0(String rgbwDeviceA0) {
        editor.putString(KEY_RGBW_DEVICE_A0, rgbwDeviceA0);
        editor.commit();
    }

    public static String getRgbwDeviceA0() {
        return pref.getString(KEY_RGBW_DEVICE_A0, null);
    }

    public static void setRgbwMasterID(String rgbwMasterID) {
        editor.putString(KEY_RGBW_MASTER_ID, rgbwMasterID);
        editor.commit();
    }

    public static String getRgbwMasterID() {
        return pref.getString(KEY_RGBW_MASTER_ID, null);
    }

}
