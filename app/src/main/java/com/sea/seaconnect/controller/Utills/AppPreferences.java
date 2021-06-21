package com.sea.seaconnect.controller.Utills;

import android.content.Context;
import android.content.SharedPreferences;

/*
 * Created by Nishant Bhardwaj on 15-06-2017.
 */

public class AppPreferences {
    private static AppPreferences appPreferences;
    private SharedPreferences sharedpref;

    public AppPreferences(Context context) {
        sharedpref=context.getSharedPreferences("spash.me", Context.MODE_PRIVATE);
    }

    public static AppPreferences init(Context context) {
        if (appPreferences == null) {
            appPreferences = new AppPreferences(context);
        }
        return appPreferences;
    }
    public void clearUserPrefs() {
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.clear();
        editor.apply();
    }
    public void putInteger(String key, int value) {
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putString(key, value);
        editor.commit();
    }



    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return sharedpref.getString(key,"");
    }

    public boolean getBoolean(String key) {
        return sharedpref.getBoolean(key,false);
    }

    public int getInteger(String key) {
        return sharedpref.getInt(key,0);
    }

}
