package com.amit.kotlib.utilities

import android.text.format.DateFormat
import android.util.Log

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by AMIT JANGID on 09-Oct-18.
 *
 *
 * this class will help in formatting date and time
**/
@Suppress("unused", "DEPRECATION", "MemberVisibilityCanBePrivate")
object DateTimeUtils
{
    private val TAG = DateTimeUtils::class.java.simpleName

    /**
     * format date time method
     *
     *
     * this method will format date time in to formats user provides
     *
     * @param dateToFormat - date time which you need to format
     * EX: 2018-10-09
     *
     * @param inDateTimeFormat - format of the date time in which you want to format given date
     * EX: dd-MM-yyyy OR dd-MM-yyyy hh:mm:ss
     *
     * @return date time in format provided
    **/
    fun formatDateTime(dateToFormat: String?, inDateTimeFormat: String): String?
    {
        if (dateToFormat != null && dateToFormat.isNotEmpty())
        {
            return DateFormat.format(inDateTimeFormat, Date(dateToFormat)).toString()
        }

        Log.e(TAG, "formatDateTime: give date cannot be parsed in given format.")
        return dateToFormat
    }

    /**
     * format date time method
     *
     *
     * this method will format date time in to formats user provides
     *
     * @param dateToFormat - date time which you need to format
     * EX: 2018-10-09
     *
     * @param inDateTimeFormat - format of the date time in which you want to format given date
     * EX: dd-MM-yyyy OR dd-MM-yyyy hh:mm:ss
     *
     * @param fromDateTimFormat - format of date time from which you want to format
     * EX: yyyy-MM-dd OR dd-MM-yyyy hh:mm:ss
     *
     * @return date time in format provided
    **/
    @Throws(ParseException::class)
    fun formatDateTime(dateToFormat: String?, inDateTimeFormat: String, fromDateTimFormat: String): String?
    {
        if (dateToFormat != null && dateToFormat.isNotEmpty())
        {
            val sdf = SimpleDateFormat(fromDateTimFormat, Locale.getDefault())
            return DateFormat.format(inDateTimeFormat, sdf.parse(dateToFormat)).toString()
        }

        Log.e(TAG, "formatDateTime: give date cannot be parsed in given format.")
        return dateToFormat
    }

    /**
     * 2018 April 27 - Friday - 04:00 PM
     * format milli seconds to time method
     *
     * this method formats the string in hh:mm:ss format
    **/
    fun formatMilliSecondsToTime(milliseconds: Long): String
    {
        val seconds = (milliseconds / 1000).toInt() % 60
        val minutes = (milliseconds / (1000 * 60) % 60).toInt()
        val hours = (milliseconds / (1000 * 60 * 60) % 24).toInt()

        return (twoDigitString(hours.toLong()) + " : " +
                twoDigitString(minutes.toLong()) + " : "
                + twoDigitString(seconds.toLong()))
    }

    /**
     * two digit string method
     *
     * this string formats the given parameter in two digits
     *
     * @param number - number to be formatted in two digits
     *
     * @return returns number in two digits in string format
    **/
    fun twoDigitString(number: Long): String
    {
        if (number == 0L)
        {
            return "00"
        }

        return if (number / 10 == 0L)
        {
            "0$number"
        }
        else number.toString()
    }

    /**
     * 2018 September 22 - Saturday - 04:38 PM
     * convert days in millis method
     *
     * this method will convert days in milliseconds
     *
     * @param days - days value in integer to be converted
     *
     * @return returns milli seconds value of given number of days
    **/
    fun convertDaysInMillis(days: Int): Long
    {
        return (days * 24 * 60 * 60 * 1000).toLong()
    }
}
