package com.designdemo.uaha.util

import android.content.Context
import android.preference.PreferenceManager
import com.designdemo.uaha.data.VersionData
import java.util.*

object PrefsUtil {
    const private val PREFS_KEY_PREFIX = "prefs_favlist_"
    const private val PREFS_KEY_NAME = "prefs_name_"
    const private val PREFS_KEY_PHONE = "prefs_phone_"
    const val PREFS_NAME_UNSET = "nameunset"

    /**
     * Toggles the value for a product to change it's Favorite state in shared prefs
     *
     * @param cxt Context
     * @param productKey productKey for an item to Favorite
     * @return
     */
    fun toggleFavorite(cxt: Context, productKey: Int): Boolean {
        val toSet: Boolean

        val prefs = PreferenceManager.getDefaultSharedPreferences(cxt)

        if (isFavorite(cxt, productKey)) {
            toSet = false
        } else {
            toSet = true
        }

        val editor = prefs.edit()
        editor.putBoolean(PREFS_KEY_PREFIX + productKey, toSet)
        editor.apply()

        return toSet
    }

    /**
     * Sets this item as a favorite in Preferences
     *
     * @param cxt
     * @param productKey
     * @return
     */
    fun isFavorite(cxt: Context, productKey: Int): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(cxt)
        return prefs.getBoolean(PREFS_KEY_PREFIX + productKey, false)
    }

    /**
     * Sets the users profile information in SharedPreferences
     *
     * @param cxt Context
     * @param name users name
     * @param phone users phone number
     * @return
     */
    fun setProfile(cxt: Context, name: String, phone: Long): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(cxt)
        val editor = prefs.edit()
        editor.putString(PREFS_KEY_NAME, name)
        editor.putLong(PREFS_KEY_PHONE, phone)
        editor.apply()

        return true
    }

    /**
     * Gets the users name, and returns @PREFS_NAME_UNSET if none set
     *
     * @param cxt
     * @return users name or default if not set
     */
    fun getName(cxt: Context): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(cxt)
        return prefs.getString(PREFS_KEY_NAME, PREFS_NAME_UNSET)
    }

    /**
     * Return phone number from Preferences, or 0 if none set
     *
     * @param cxt
     * @return phone number or 0 if not set
     */
    fun getPhone(cxt: Context): Long {
        val prefs = PreferenceManager.getDefaultSharedPreferences(cxt)
        return prefs.getLong(PREFS_KEY_PHONE, 0)
    }


    /**
     * Returns list of verbose product names for favorites
     *
     * @param cxt
     * @return list of verbose product names
     */
    fun getFavorites(cxt: Context?): ArrayList<String> {
        val list = ArrayList<String>()

        for (osIndex in VersionData.osVersions) {
            if (isFavorite(cxt!!,  osIndex)) {
                list.add(VersionData.getProductName(osIndex))
            }
        }
        for (deviceIndex in VersionData.deviceVersions) {
            if (isFavorite(cxt!!,  deviceIndex)) {
                list.add(VersionData.getProductName(deviceIndex))
            }
        }
        return list
    }

}
