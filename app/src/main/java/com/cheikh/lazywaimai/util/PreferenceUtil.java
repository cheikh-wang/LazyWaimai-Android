package com.cheikh.lazywaimai.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.List;
import java.util.Set;

public class PreferenceUtil {

    private static SharedPreferences mPreferences;
    private static Gson mGson;

    private PreferenceUtil() {
    }

    public static void init(Application application, Gson gson) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        mGson = gson;
    }

    public static boolean set(String key, boolean value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }

    public static boolean set(String key, String value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getString(String name) {
        return getString(name, null);
    }

    public static String getString(String name, String defaultValue) {
        return mPreferences.getString(name, defaultValue);
    }

    public static boolean set(String key, int value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return mPreferences.getInt(key, defaultValue);
    }

    public static boolean set(String key, long value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static long getLong(String key) {
        return getLong(key, 0L);
    }

    public static long getLong(String key, long defaultValue) {
        return mPreferences.getLong(key, defaultValue);
    }

    public static void set(String key, Object obj) {
        try {
            String e = mGson.toJson(obj);
            set(key, e);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static <T> T getObject(String name, Class<T> clazz) {
        T obj = null;

        try {
            String e = getString(name);
            obj = mGson.fromJson(e, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    public static <T> void set(String key, List<T> list) {
        try {
            String e = mGson.toJson(list);
            set(key, e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean set(String name, Set<String> flag) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putStringSet(name, flag);
        return editor.commit();
    }

    public static Set<String> getStringSet(String name) {
        return mPreferences.getStringSet(name, null);
    }
}
