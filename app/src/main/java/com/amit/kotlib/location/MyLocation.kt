package com.amit.kotlib.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import android.util.Log
import java.util.*

/**
 * Created by RAMCHANDRA SINGH on 10-03-2017.
**/
@Suppress("unused")
class MyLocation
{
    private var timer1: Timer? = null
    private var locationManager: LocationManager? = null
    private var locationResult: LocationResult? = null
    private var gpsEnabled = false
    private var networkEnabled = false
    private var mContext: Context? = null

    private val locationListenerNetwork = object : LocationListener
    {
        override fun onLocationChanged(location: Location)
        {
            timer1!!.cancel()
            locationResult!!.gotLocation(location)

            if (ActivityCompat.checkSelfPermission(mContext!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                Log.e(TAG, "onLocationChanged: permission to access location is not granted.")
                return
            }

            locationManager!!.removeUpdates(this)
            locationManager!!.removeUpdates(locationListenerGps)
        }

        override fun onProviderDisabled(provider: String)
        {

        }

        override fun onProviderEnabled(provider: String)
        {

        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle)
        {

        }
    }

    private val locationListenerGps = object : LocationListener
    {
        override fun onLocationChanged(location: Location)
        {
            timer1!!.cancel()
            locationResult!!.gotLocation(location)

            if (ActivityCompat.checkSelfPermission(mContext!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                Log.e(TAG, "onLocationChanged: permission to access location is not granted.")
                return
            }

            locationManager!!.removeUpdates(this)
            // locationManager!!.removeUpdates(this)
        }

        override fun onProviderDisabled(provider: String)
        {

        }

        override fun onProviderEnabled(provider: String)
        {

        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle)
        {

        }
    }

    fun getLocation(context: Context, result: LocationResult): Boolean
    {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        this.mContext = context
        locationResult = result

        if (locationManager == null)
        {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        //exceptions will be thrown if provider is not permitted.
        try
        {
            gpsEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }
        catch (ex: Exception)
        {
            Log.e(TAG, "getLocation: exception while checking for gps provider:\n")
            ex.printStackTrace()
        }

        try
        {
            networkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }
        catch (ex: Exception)
        {
            Log.e(TAG, "getLocation: exception while checking for network provider:\n")
            ex.printStackTrace()
        }

        //don't start listeners if no provider is enabled
        if (!gpsEnabled && !networkEnabled)
        {
            return false
        }

        if (ActivityCompat.checkSelfPermission(this.mContext!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.mContext!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Log.e(TAG, "onLocationChanged: permission to access location is not granted.")
            return false
        }

        if (gpsEnabled)
        {
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListenerGps)
        }

        if (networkEnabled)
        {
            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListenerNetwork)
        }

        timer1 = Timer()
        timer1!!.schedule(GetLastLocation(), 10000)
        return true
    }

    abstract class LocationResult
    {
        abstract fun gotLocation(location: Location?)
    }

    internal inner class GetLastLocation : TimerTask()
    {
        override fun run()
        {
            var netLoc: Location? = null
            var gpsLoc: Location? = null

            if (ActivityCompat.checkSelfPermission(mContext!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                Log.e(TAG, "onLocationChanged: permission to access location is not granted.")
                return
            }

            if (gpsEnabled)
            {
                gpsLoc = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }

            if (networkEnabled)
            {
                netLoc = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            //if there are both values use the latest one
            if (gpsLoc != null && netLoc != null)
            {
                if (gpsLoc.time > netLoc.time)
                {
                    locationResult!!.gotLocation(gpsLoc)
                }
                else
                {
                    locationResult!!.gotLocation(netLoc)
                }

                return
            }

            if (gpsLoc != null)
            {
                locationResult!!.gotLocation(gpsLoc)
                return
            }

            if (netLoc != null)
            {
                locationResult!!.gotLocation(netLoc)
                return
            }

            locationResult!!.gotLocation(null)
        }
    }

    /**
     * 2018 October 17 - Wednesday - 02:50 PM
     * is gps enabled method
     *
     * this method will check if gps is enabled or not
     *
     * @param context - context of the application
     *
     * @return true if enabled, false if not enabled.
    */
    @Suppress("DEPRECATION")
    fun isGPSEnabled(context: Context): Boolean
    {
        val locationMode: Int
        val locationProviders: String

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            try
            {
                locationMode = Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
            }
            catch (e: Settings.SettingNotFoundException)
            {
                e.printStackTrace()
                return false
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF
        }
        else
        {
            locationProviders = Settings.Secure.getString(context.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
            return !TextUtils.isEmpty(locationProviders)
        }
    }

    companion object
    {
        private val TAG = MyLocation::class.java.simpleName
    }
}
