package com.bruce.note.greennote.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bruce.note.greennote.BaseApp;

/**
 * Created by liq on 2017/8/3.
 */

public class AppPref {

//    public static final String base_url = "http://60.29.252.83:8087/";
//    public static final String base_url = "http://36.110.66.49:58900/";


    private static SharedPreferences prefs() {
        return PreferenceManager.getDefaultSharedPreferences(BaseApp.getContext());
    }

    public static void setAvatarPicture(String path) {
        setString(DefaultPrefKey.AVATAR_PATH, path);
    }

    public static String getAvatarPicture() {
        return getString(DefaultPrefKey.AVATAR_PATH);
    }

//    public static void setRemoteSetting(String extData) {
//        setString(DefaultPrefKey.REMOTE_SETTING, extData);
//        LogUtils.sf("设置服务器地址:" + extData);
//    }
//
//    public static String getRemoteSetting() {
//        String url = getString(DefaultPrefKey.REMOTE_SETTING, base_url);
//        LogUtils.sf("获取服务器地址:" + url);
//        return url;
//    }

    public enum DefaultPrefKey implements PrefKey {
        // 是否第一次登录
        FIRST_KEY,
        USER_KEY,//key
        USER_NAME,
        USER_PWD,
        USER_ALIAS,
        EN_PWD,
        LOGIN_INFO,
        AVATAR_PATH,
        REMOTE_SETTING // 服务器设置
    }

    public interface PrefKey {
        String name();

        String toString();
    }

    private static String getString(PrefKey key) {
        return getString(key, "");
    }

    private static String getString(PrefKey key, String defaultValue) {
        return prefs().getString(key.name(), defaultValue);
    }

    private static void setString(PrefKey key, String value) {
        SharedPreferences.Editor editor = prefs().edit();
        if (TextUtils.isEmpty(value)) {
            editor.remove(key.name());
        } else {
            editor.putString(key.name(), value);
        }
        editor.apply();
    }

    public static long getLong(PrefKey key) {
        try {
            String value = getString(key);
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void setLong(PrefKey key, long value) {
        setString(key, Long.toString(value));
    }

    private static int getInt(PrefKey key, int def) {
        try {
            String value = getString(key);
            if (value.isEmpty()) {
                return def;
            }
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static int getInt(PrefKey key) {
        return getInt(key, 0);
    }

    public static void setInt(PrefKey key, int value) {
        setString(key, Integer.toString(value));
    }

    public static boolean getBoolean(PrefKey key, boolean def) {
        String value = getString(key, Boolean.toString(def));
        return Boolean.parseBoolean(value);
    }

    public static void setBoolean(PrefKey key, boolean value) {
        setString(key, Boolean.toString(value));
    }

    public static void remove(PrefKey key) {
        prefs().edit().remove(key.name()).apply();
    }

    public static boolean keyExists(@NonNull PrefKey key) {
        return prefs().contains(key.name());
    }

    /**
     * 是否第一次登录
     *
     * @return
     */
    public static boolean getFirstLogin() {
        return getBoolean(DefaultPrefKey.FIRST_KEY, true);
    }


    public static void setFirstLogin(boolean b) {
        setBoolean(DefaultPrefKey.FIRST_KEY, b);
    }

    /**
     * USER_ALIAS
     *
     * @param alias
     */
    public static void setAlias(String alias) {
        setString(DefaultPrefKey.USER_ALIAS, alias);
    }

    /**
     * 获取 USER_ALIAS
     *
     * @return
     */
    public static String getAlias() {
        return getString(DefaultPrefKey.USER_ALIAS, "");
    }

    /**
     * 设置USERKEY
     *
     * @param key
     */
    public static void setKey(String key) {
        setString(DefaultPrefKey.USER_KEY, key);
    }

    /**
     * 获取 KEY
     *
     * @return
     */
    public static String getKey() {
        return getString(DefaultPrefKey.USER_KEY, "");
    }

    /**
     * @param key
     */
    public static void setName(String key) {
        setString(DefaultPrefKey.USER_NAME, key);
    }

    /**
     * @return
     */
    public static String getName() {
        return getString(DefaultPrefKey.USER_NAME, "");
    }

    public static String getPwd() {
        return getString(DefaultPrefKey.USER_PWD, "");
    }

    public static void setPwd(String key) {
        setString(DefaultPrefKey.USER_PWD, key);
    }

    public static String getEnPwd() {
        return getString(DefaultPrefKey.EN_PWD, "");
    }

    public static void setEnPwd(String en_pwd) {
        setString(DefaultPrefKey.EN_PWD, en_pwd);
    }

    public static void setLoginInfo(String info) {
        setString(DefaultPrefKey.LOGIN_INFO, info);
    }

    public static String getLoginInfo() {
        return getString(DefaultPrefKey.LOGIN_INFO, "");
    }

    public static void clearUserInfo() {
//        remove(DefaultPrefKey.USER_PWD);
        remove(DefaultPrefKey.USER_KEY);
        remove(DefaultPrefKey.AVATAR_PATH);
    }

    public static void clearKey() {
        remove(DefaultPrefKey.USER_KEY);
    }
}
