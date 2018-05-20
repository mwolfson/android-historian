package com.designdemo.uaha;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mwolfson on 8/6/15.
 */
public class PrefsUtil {
    private static final String PREFS_KEY_PREFIX = "prefs_favlist_";
    private static final String PREFS_KEY_NAME = "prefs_name_";
    private static final String PREFS_KEY_PHONE = "prefs_phone_";

    public static final String PREFS_NAME_UNSET = "nameunset";


    public static boolean toggleFavorite(Context cxt, int productKey) {
        boolean toSet;

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);

        if (isFavorite(cxt, productKey)) {
            toSet = false;
        } else {
            toSet = true;
        }

        final SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREFS_KEY_PREFIX + productKey, toSet);
        editor.apply();

        return toSet;
    }

    public static boolean isFavorite(Context cxt, int productKey) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);
        return prefs.getBoolean(PREFS_KEY_PREFIX + productKey, false);
    }

    public static boolean setProfile (Context cxt, String name, long phone) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_KEY_NAME, name);
        editor.putLong(PREFS_KEY_PHONE, phone);
        editor.apply();

        return true;
    }

    public static String getName(Context cxt) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);
        return prefs.getString(PREFS_KEY_NAME, PREFS_NAME_UNSET);
    }

    public static long getPhone(Context cxt) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);
        return prefs.getLong(PREFS_KEY_PHONE, 0);
    }


    /**
     * Returns list of verbose product names for favorites
     *
     * @param cxt
     * @return list of verbose product names
     */
    public static ArrayList<String> getFavorites(Context cxt) {
        ArrayList<String> list = new ArrayList<>();

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);

        //Clunky I know
        for (int x = 0; x < VersionData.osVersions.length; x++) {
            int productInt = VersionData.osVersions[x];
            if (isFavorite(cxt, productInt)) {
                list.add(VersionData.getProductName(productInt));
                Log.d("PREFS", "Setting a favorite in list: " + VersionData.getProductName(productInt));
            }
        }

        for (int x = 0; x < VersionData.deviceVersions.length; x++) {
            int productInt = VersionData.deviceVersions[x];
            if (isFavorite(cxt, productInt)) {
                list.add(VersionData.getProductName(productInt));
                Log.d("PREFS", "Setting a favorite in list: " + VersionData.getProductName(productInt));
            }
        }

        return list;
    }

}
