package com.amit.kotlib.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import java.util.Locale

/**
 * 2018 April 21 - Saturday - 04:27 PM
 * Location Address class
 *
 * this class helps to get the address of the current location
 * using the latitude and longitude provided
 */
object LocationAddress {
    private val TAG = LocationAddress::class.java.simpleName

    /**
     * 2018 April 21 - Saturday - 04:28 PM
     * Get Address from location method
     *
     * this method will get the address from current location
     * using the latitude and longitude and
     * will return the complete address of current location
     */
    fun getAddressFromLocation(latitude: Double, longitude: Double,
                               context: Context, handler: Handler) {
        val thread = object : Thread() {
            override fun run() {
                val result: String
                var address: Address? = null
                val geocoder = Geocoder(context, Locale.getDefault())

                try {
                    // getting the list of address from latitude and longitude
                    val addressList = geocoder.getFromLocation(latitude, longitude, 1)

                    if (addressList != null && addressList.size > 0) {
                        address = addressList[0]
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "getAddressFromLocation:run: exception while getting address from location")
                    e.printStackTrace()
                } finally {
                    val message = Message.obtain()
                    message.target = handler

                    if (address != null) {
                        message.what = 1
                        val bundle = Bundle()

                        bundle.putString("thoroughFare", address.thoroughfare)
                        bundle.putString("subThoroughFare", address.subThoroughfare)
                        bundle.putString("city", address.locality)
                        bundle.putString("state", address.adminArea)
                        bundle.putString("country", address.countryName)
                        bundle.putString("postalCode", address.postalCode)
                        bundle.putString("subAdminArea", address.subAdminArea)
                        bundle.putString("subLocality", address.subLocality)
                        message.data = bundle
                    } else {
                        message.what = 1
                        val bundle = Bundle()

                        result = "Latitude: " + latitude + "Longitude: " + longitude +
                                "\n Unable to get address for this location."

                        bundle.putString("address", result)
                        message.data = bundle
                    }

                    message.sendToTarget()
                }
            }
        }

        thread.start()
    }
}
