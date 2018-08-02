package com.amit.kotlib.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/*
 * Created by AMIT JANGID
 * 2018 April 17 - Tuesday - 12:20 PM
 *
 * this class is useful for saving data in shared preferences
 */

class SharedPreferenceData
/**
 * Constructor of the class
 *
 * @param context - this will take the context of the application
 */
(context: Context) {
    private val mSharedPreference: SharedPreferences
    private val mEditor: SharedPreferences.Editor

    init {
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
        mEditor = mSharedPreference.edit()
    }

    /**
     * Set value method
     * this method is used for saving the data in shared preferences
     *
     * @param key   - key is the name of the value will store data for and use it later to retrieve
     * key will be unique for every data you need to save.
     * @param value - value will the data for the key as a string format.
     */
    fun setValue(key: String, value: String) {
        mEditor.putString(key, value)
        mEditor.commit()
    }

    /**
     * Get value method
     * this method is used for getting the data from shared preferences
     *
     * @param key - you need to just pass the key name of the data you want to get.
     * @return 0 - this method will return 0 as default value
     * if not record found for the key or key not found
     * this will return the data in string format
     */
    fun getValue(key: String): String {
        return mSharedPreference.getString(key, "0")
    }

    /**
     * Set String value method
     * this method is used for saving the data in shared preferences
     *
     * @param key   - key is the name of the value will store data for and use it later to retrieve
     * key will be unique for every data you need to save.
     * @param value - value will the data for the key as a string format.
     */
    fun setStrValue(key: String, value: String) {
        mEditor.putString(key, value)
        mEditor.commit()
    }

    /**
     * Get String value method
     * this method is used for getting the data from shared preferences
     *
     * @param key - you need to just pass the key name of the data you want to get.
     * @return 0 - this method will return 0 as default value
     * if not record found for the key or key not found
     * this will return the data in string format
     */
    fun getStrValue(key: String): String {
        return mSharedPreference.getString(key, "0")
    }

    /**
     * Set Int value method
     * this method is used for saving the data in shared preferences
     *
     * @param key   - key is the name of the value will store data for and use it later to retrieve
     * key will be unique for every data you need to save.
     * @param value - value will the data for the key in integer format.
     */
    fun setIntValue(key: String, value: Int) {
        mEditor.putInt(key, value)
        mEditor.commit()
    }

    /**
     * Get int value method
     * this method is used for getting the data from shared preferences
     *
     * @param key - you need to just pass the key name of the data you want to get.
     * @return 0 - this method will return 0 as default value
     * if not record found for the key or key not found
     * this will return data in integer format.
     */
    fun getIntValue(key: String): Int {
        return mSharedPreference.getInt(key, 0)
    }

    /**
     * Set boolean value method
     * this method is used for saving the data in shared preferences
     *
     * @param key   - key is the name of the value will store data for and use it later to retrieve
     * key will be unique for every data you need to save.
     * @param value - value will the data for the key in boolean format.
     */
    fun setBooleanValue(key: String, value: Boolean) {
        mEditor.putBoolean(key, value)
        mEditor.commit()
    }

    /**
     * Get boolean value method
     * this method is used for getting the data from shared preferences
     *
     * @param key - you need to just pass the key name of the data you want to get.
     * @return 0 - this method will return 0 as default value
     * if not record found for the key or key not found
     * this will return data in boolean format.
     */
    fun getBooleanValue(key: String): Boolean {
        return mSharedPreference.getBoolean(key, false)
    }

    /**
     * Set float value method
     * this method is used for saving the data in shared preferences
     *
     * @param key   - key is the name of the value will store data for and use it later to retrieve
     * key will be unique for every data you need to save.
     * @param value - value will the data for the key in float format.
     */
    fun setFloatValue(key: String, value: Float) {
        mEditor.putFloat(key, value)
        mEditor.commit()
    }

    /**
     * Get float value method
     * this method is used for getting the data from shared preferences
     *
     * @param key - you need to just pass the key name of the data you want to get.
     * @return 0 - this method will return 0 as default value
     * if not record found for the key or key not found
     * this will return data in float format.
     */
    fun getFloatValue(key: String): Float {
        return mSharedPreference.getFloat(key, 0f)
    }

    /**
     * Set Long value method
     * this method is used for saving the data in shared preferences
     *
     * @param key   - key is the name of the value will store data for and use it later to retrieve
     * key will be unique for every data you need to save.
     * @param value - value will the data for the key in Long format.
     */
    fun setLongValue(key: String, value: Long) {
        mEditor.putLong(key, value)
        mEditor.commit()
    }

    /**
     * Get Long value method
     * this method is used for getting the data from shared preferences
     *
     * @param key - you need to just pass the key name of the data you want to get.
     * @return 0 - this method will return 0 as default value
     * if not record found for the key or key not found
     * this will return data in Long format.
     */
    fun getLongValue(key: String): Long {
        return mSharedPreference.getLong(key, 0)
    }

    /**
     * Set String set value method
     * this method is used for saving the data in shared preferences
     *
     * @param key   - key is the name of the value will store data for and use it later to retrieve
     * key will be unique for every data you need to save.
     * @param value - value will the data for the key in string set format.
     */
    fun setStrSetValue(key: String, value: Set<String>) {
        mEditor.putStringSet(key, value)
        mEditor.commit()
    }

    /**
     * Get string set value method
     * this method is used for getting the data from shared preferences
     *
     * @param key - you need to just pass the key name of the data you want to get.
     * @return 0 - this method will return 0 as default value
     * if not record found for the key or key not found
     * this will return data in string set format.
     */
    fun getStrSetValue(key: String): Set<String>? {
        return mSharedPreference.getStringSet(key, null)
    }
}
