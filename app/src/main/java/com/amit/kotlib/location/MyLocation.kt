package com.amit.kotlib.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log

import java.util.Timer
import java.util.TimerTask

/**
 * Created by RAMCHANDRA SINGH on 10-03-2017.
 */
class MyLocation {
    private var timer1: Timer? = null
    private var locationManager: LocationManager? = null
    private var locationResult: LocationResult? = null
    private var gps_enabled = false
    private var network_enabled = false
    private var mContext: Context? = null

    private val TAG = "MyLocation"
    private val REQUEST_CHECK_SETTINGS = 1

    private val locationListenerNetwork = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            timer1!!.cancel()
            locationResult!!.gotLocation(location)

            if (ActivityCompat.checkSelfPermission(mContext!!,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "onLocationChanged: permission to access location is not granted.")
                return
            }

            locationManager!!.removeUpdates(this)
            locationManager!!.removeUpdates(locationListenerGps)
        }

        override fun onProviderDisabled(provider: String) {

        }

        override fun onProviderEnabled(provider: String) {

        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

        }
    }

    private val locationListenerGps = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            timer1!!.cancel()
            locationResult!!.gotLocation(location)

            if (ActivityCompat.checkSelfPermission(mContext!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "onLocationChanged: permission to access location is not granted.")
                return
            }

            locationManager!!.removeUpdates(this)
            // locationManager!!.removeUpdates(this)
        }

        override fun onProviderDisabled(provider: String) {

        }

        override fun onProviderEnabled(provider: String) {

        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

        }
    }

    fun getLocation(context: Context, result: LocationResult): Boolean {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        this.mContext = context
        locationResult = result

        if (locationManager == null) {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        //exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
            Log.e(TAG, "getLocation: exception while checking for gps provider:\n")
            ex.printStackTrace()
        }

        try {
            network_enabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
            Log.e(TAG, "getLocation: exception while checking for network provider:\n")
            ex.printStackTrace()
        }

        //don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled) {
            return false
        }

        if (ActivityCompat.checkSelfPermission(this.mContext!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.mContext!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "onLocationChanged: permission to access location is not granted.")
            return false
        }

        if (gps_enabled) {
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListenerGps)
        }

        if (network_enabled) {
            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListenerNetwork)
        }

        timer1 = Timer()
        timer1!!.schedule(GetLastLocation(), 10000)
        return true
    }

    abstract class LocationResult {
        abstract fun gotLocation(location: Location?)
    }

    internal inner class GetLastLocation : TimerTask() {
        override fun run() {
            var net_loc: Location? = null
            var gps_loc: Location? = null

            if (ActivityCompat.checkSelfPermission(mContext!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "onLocationChanged: permission to access location is not granted.")
                return
            }

            if (gps_enabled) {
                gps_loc = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }

            if (network_enabled) {
                net_loc = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            //if there are both values use the latest one
            if (gps_loc != null && net_loc != null) {
                if (gps_loc.time > net_loc.time) {
                    locationResult!!.gotLocation(gps_loc)
                } else {
                    locationResult!!.gotLocation(net_loc)
                }

                return
            }

            if (gps_loc != null) {
                locationResult!!.gotLocation(gps_loc)
                return
            }

            if (net_loc != null) {
                locationResult!!.gotLocation(net_loc)
                return
            }

            locationResult!!.gotLocation(null)
        }
    }
}
