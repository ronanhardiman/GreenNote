package com.bruce.note.greennote.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bruce.note.greennote.BaseApp;

import timber.log.Timber;

import static com.bruce.note.greennote.utils.AppPrefs.DefaultPrefKey.USER_KEY;


/**
 * Created by liq on 2017/7/10.
 */

public class AppPrefs {

    public static SharedPreferences prefs() {
        return PreferenceManager.getDefaultSharedPreferences(BaseApp.getContext());
    }

    public enum DefaultPrefKey implements PrefKey {
        // name of last shown activity
        GENDER_KEY,
        USER_KEY,
        REST_KEY
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

    private static long getLong(PrefKey key) {
        try {
            String value = getString(key);
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static void setLong(PrefKey key, long value) {
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

    public static String getString(String key) {
        return prefs().getString(key,"");
    }

    public static void setString(String key, String values) {
        SharedPreferences.Editor edit = prefs().edit();
        edit.putString(key, values);
        edit.apply();
    }

    public static void setLong(String key, Long values) {
        Timber.d("setLong long %s",values);
        SharedPreferences.Editor edit = prefs().edit();
        edit.putLong(key, values);
        edit.apply();
    }

    public static Long getLong(String key) {
        return prefs().getLong(key,0L);
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

    private static void remove(PrefKey key) {
        prefs().edit().remove(key.name()).apply();
    }

    public static boolean keyExists(@NonNull PrefKey key) {
        return prefs().contains(key.name());
    }

    /**
     * 获取男女
     *
     * @return
     */
    public static int getGenderIndex() {
        return getInt(DefaultPrefKey.GENDER_KEY);
    }

    /**
     * 设置男女
     * 0,1
     * @param gender
     * @return
     */
    public static void setGenderIndex(int gender) {
        setInt(DefaultPrefKey.GENDER_KEY, gender);
    }

    public static String getCurrentUse() {
        return getString(USER_KEY);
    }

    public static void setCurrentUse(String string) {
        if (!TextUtils.isEmpty(string)) {
            setString(USER_KEY,string);
        }
    }
}
