package com.amit.kotlib.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.support.annotation.CheckResult
import android.util.Log

import android.content.Context.CONNECTIVITY_SERVICE

/**
 * Created By AMIT JANGID
 * 2018 April 17 - Tuesday - 12:50 PM
 */
object Internet {
    private val TAG = Internet::class.java.simpleName

    /**
     * isInternetConnected
     * this method will check for connectivity service
     *
     * @param context - context of the application
     * @return - returns true or false.
     */
    @CheckResult
    fun isInternetConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        if (connectivityManager != null) {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        } else {
            return false
        }
    }

    /**
     * isMobileNetConnected
     * this method helps to find out whether the user is connected to mobile internet or not
     *
     * @param context - context of the application
     * @return this returns true or false
     * true if connected or connecting
     * false if not connected.
     */
    @CheckResult
    fun isMobileNetConnected(context: Context): Boolean {
        try {
            val manager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

            if (manager != null) {
                return manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting
            } else {
                Log.e(TAG, "isMobileInternetAvailable: connectivity manager was null.")
                return false
            }
        } catch (e: Exception) {
            Log.e(TAG, "isMobileInternetAvailable: exception while checking for mobile internet.")
            return false
        }

    }

    /**
     * isWifiConnected
     * this method will check if the device is connected to a wifi
     *
     * @param context - context of the application
     * @return - this returns true or false
     * true if connected
     * false if not connected
     */
    @CheckResult
    fun isWifiConnected(context: Context): Boolean {
        try {
            val manager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

            if (manager != null) {
                return manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting
            } else {
                Log.e(TAG, "isWifiConnected: connectivity manager is null.")
                return false
            }
        } catch (e: Exception) {
            Log.e(TAG, "isWifiConnected: exception while checking for wifi connection.")
            e.printStackTrace()
            return false
        }

    }
}
