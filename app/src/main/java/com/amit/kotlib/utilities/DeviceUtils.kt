package com.amit.kotlib.utilities

import android.Manifest
import android.Manifest.permission.ACCESS_WIFI_STATE
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.support.annotation.CheckResult
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.util.Log
import android.view.WindowManager
import java.net.NetworkInterface
import java.util.*

/**
 * Created by AMIT JANGID on 09-Oct-18.
 *
 * this class will help in getting information related to devices
**/
@SuppressLint("HardwareIds")
@Suppress("unused", "DEPRECATION", "NAME_SHADOWING")
object DeviceUtils
{
    private val TAG = DeviceUtils::class.java.simpleName

    /**
     * is Sd Card Mounted
     * this method will check if sd card is mounted or not
     *
     * @return - true or false
     * if sd card available then will return true
     * else will return false
    **/
    val isSdCardMounted: Boolean
        @CheckResult
        get()
        {
            return try
            {
                val status = Environment.getExternalStorageState()
                status == Environment.MEDIA_MOUNTED
            }
            catch (e: Exception)
            {
                Log.e(TAG, "isSdCardMounted: exception while checking for sd card mounted.")
                e.printStackTrace()
                false
            }
        }

    /**
     * get device name method
     *
     * this method will get the name of the device
     * @return name of the device with manufacturer
    **/
    val deviceName: String
        get()
        {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL

            return if (model.startsWith(manufacturer))
            {
                TextUtils.capitalizeString(model)
            }
            else
            {
                TextUtils.capitalizeString("$manufacturer $model")
            }
        }

    /**
     * 2018 April 02 - Monday - 06:21 PM
     * Get IMEI Number method
     *
     * this method gets IMEI number after getting the permission.
     *
     * @return - it will return IMEI number if permission granted
     * else if no permission granted then will return empty string.
     */
    @CheckResult
    fun getIMEINumber(context: Context): String
    {
        try
        {
            val imeiNumber: String
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            // checking if read phone state permission given or not
            // if yes the getting the imei number
            // else asking for permission
            if (ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    imeiNumber = telephonyManager.imei
                    Log.e(TAG, "getIMEINumber: IMEI Number is: $imeiNumber")
                }
                else
                {
                    imeiNumber = telephonyManager.deviceId
                    Log.e(TAG, "getIMEINumber: Device Id is: $imeiNumber")
                }

                return imeiNumber
            }
            else
            {
                Log.e(TAG, "getIMEINumber: READ_PHONE_STATE permission not granted.")
                return ""
            }
        }
        catch (e: Exception)
        {
            Log.e(TAG, "getIMEINumber: expection while getting imei number:\n")
            e.printStackTrace()
            return ""
        }
    }

    /**
     * 2018 September 18 - Tuesday - 04:54 PM
     * get battery percentage method
     *
     * this method will get the percentage of battery remaining
     *
     * @param context - context of the application
     * @return battery percentage in int or 0
    **/
    @CheckResult
    fun getBatteryPercentage(context: Context): Int
    {
        try
        {
            val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = context.registerReceiver(null, intentFilter)

            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

            val batteryPercentage = level / scale.toFloat()
            val batteryLevel = (batteryPercentage * 100).toInt()

            Log.e(TAG, "getBatteryPercentage: current battery level is: $batteryLevel")
            return batteryLevel

        }
        catch (e: Exception)
        {
            Log.e(TAG, "getBatteryPercentage: exception while getting battery percentage:\n")
            e.printStackTrace()
            return 0
        }
    }

    /**
     * get device id method
     *
     * this method will get the device id
     *
     * @param context - application context
     * @return device id in string
    **/
    fun getDeviceID(context: Context): String
    {
        // this will get the device id of the device
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     * 2018 October 04 - Thursday - 02:50 PM
     * get mac address method
     *
     * this method will get mac address of the device
    **/
    fun getMacAddress(context: Context): String
    {
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (ContextCompat.checkSelfPermission(context, ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
                {
                    Log.e(TAG, "getMacAddress: access wifi state permission not granted.")
                    return ""
                }
            }

            val interfaceName = "wlan0"
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())

            for (networkInterface in interfaces)
            {
                if (!networkInterface.name.equals(interfaceName, ignoreCase = true))
                {
                    continue
                }

                val mac = networkInterface.hardwareAddress ?: return ""
                val builder = StringBuilder()

                for (aMac in mac)
                {
                    builder.append(String.format("%02X:", aMac))
                }

                if (builder.isNotEmpty())
                {
                    builder.deleteCharAt(builder.length - 1)
                }

                Log.e(TAG, "getMacAddress: mac address of device is: " + builder.toString())
                return builder.toString()
            }

            return ""
        }
        catch (e: Exception)
        {
            Log.e(TAG, "getMacAddress: exception while getting mac address:\n")
            e.printStackTrace()
            return ""
        }
    }

    /**
     * get Time with AM/PM Method
     *
     * This method will show the time in two digits and also am pm
     * if the time selected is afternoon 02:00 then it will show 02:00 PM
     * else of the time selected is night 02:00 then it will show 02:00 AM
     *
     * @param hours - hours to convert
     * @param minutes - minutes to convert
     *
     * @return String with time appended with AM/PM
    **/
    fun getTimeWithAMPM(hours: Int, minutes: Int): String
    {
        var hours = hours

        try
        {
            var timeStamp = "AM"
            val time: String

            when
            {
                hours > 12 ->
                {
                    timeStamp = "PM"
                    hours -= 12
                }

                hours == 0 -> hours += 12
                hours == 12 -> timeStamp = "PM"
            }

            time = String.format(Locale.getDefault(), "%02d", hours) + ":" +
                    String.format(Locale.getDefault(), "%02d", minutes) + " " + timeStamp

            return time
        }
        catch (e: Exception)
        {
            Log.e("Exception", "in show time with am pm method in generate qr code activity:\n")
            e.printStackTrace()
            return ""
        }
    }

    /**
     * get screen size
     * this method will get the size of the screen
     *
     * @param context - context of the application
     * @return size of the screen as Point
    **/
    fun getScreenSize(context: Context): Point
    {
        val point = Point()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        wm.defaultDisplay?.getSize(point)

        return point
    }
}
