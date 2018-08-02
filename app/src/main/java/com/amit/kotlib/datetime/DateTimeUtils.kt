package com.amit.kotlib.datetime

import android.content.Context
import android.text.format.DateUtils
import android.util.Log

import com.amit.kotlib.R

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
 * DateTimeUtils
 * This class contains a bunch of function that can manipulate
 * Date object or Date String to achieve certain operations
 * e.g : Time difference, Time Ago, Date formatting
 *
 */
object DateTimeUtils {
    private val TAG = DateTimeUtils::class.java.simpleName

    private var debug: Boolean = false
    private var timeZone = "UTC"

    /**
     * Enable / Disable debug
     * this method enables ro disables debug
     *
     * @param state Debug state
     */
    fun setDebug(state: Boolean) {
        debug = state
    }

    /**
     * Set Time Zone
     * this method sets the time zone.
     */
    fun setTimeZone(zone: String) {
        timeZone = zone
    }

    /**
     * Get Date or DateTime formatting pattern
     *
     * @param dateString Date String
     * @return Format Pattern
     */
    private fun getDatePattern(dateString: String?): String {
        return if (isDateTime(dateString)) {
            if (dateString!!.contains("/")) DateTimeFormat.DATE_TIME_PATTERN_2 else DateTimeFormat.DATE_TIME_PATTERN_1
        } else {
            if (dateString!!.contains("/")) DateTimeFormat.DATE_PATTERN_2 else DateTimeFormat.DATE_PATTERN_1
        }
    }

    /**
     * Convert a Java Date object to String
     *
     * @param date Date Object
     * @param locale Locale
     * @return Date Object string representation
     */
    @JvmOverloads
    fun formatDateToString(date: Date?, locale: Locale = Locale.getDefault()): String {
        if (date == null && debug) {
            Log.e(TAG, "formatDateToString: date value is null.")
        }

        val sdf = SimpleDateFormat(DateTimeFormat.DATE_TIME_PATTERN_1, locale)
        sdf.timeZone = TimeZone.getTimeZone(timeZone)
        return sdf.format(date)
    }

    /**
     * Convert a date string to Java Date Object
     *
     * @param dateString Date String
     * @param locale Locale
     * @return Java Date Object
     */
    @JvmOverloads
    fun formatStringToDate(dateString: String?, locale: Locale = Locale.getDefault()): Date? {
        val sdf = SimpleDateFormat(getDatePattern(dateString), locale)
        sdf.timeZone = TimeZone.getTimeZone(timeZone)
        var date: Date? = null

        if (dateString != null) {
            try {
                date = sdf.parse(dateString)
            } catch (e: ParseException) {
                Log.e(TAG, "formatStringToDate: Failed to parse supplied date string: $dateString")
                e.printStackTrace()
            }

        }

        return date
    }

    /**
     * Convert a timeStamp into a date object
     *
     * @param timeStamp TimeStamp
     * @param units Witch unit is whether seconds or milliseconds
     * @see DateTimeUnits.SECONDS
     *
     * @see DateTimeUnits.MILLISECONDS
     *
     * @return Date object
     */
    @JvmOverloads
    fun formatTimeStampToDate(timeStamp: Long, units: DateTimeUnits = DateTimeUnits.MILLISECONDS): Date {
        return if (units == DateTimeUnits.SECONDS) {
            Date(timeStamp * 1000L)
        } else {
            Date(timeStamp)
        }
    }

    /**
     * Format date using a given pattern
     * and apply supplied locale
     *
     * @param date Date Object
     * @param pattern Pattern
     * @param locale Locale
     * @return Formatted date
     */
    @JvmOverloads
    fun formatWithPattern(date: Date?, pattern: String, locale: Locale = Locale.getDefault()): String {
        if (date == null && debug) {
            Log.e(TAG, "formatWithPattern: date is null")
        }

        val sdf = SimpleDateFormat(pattern, locale)
        sdf.timeZone = TimeZone.getTimeZone(timeZone)
        return sdf.format(date)
    }

    /**
     * Format date using a given pattern
     * and apply supplied locale
     *
     * @param date Date String
     * @param pattern Pattern
     * @param locale Locale
     * @return Formatted date
     */
    @JvmOverloads
    fun formatWithPattern(date: String, pattern: String, locale: Locale = Locale.getDefault()): String {
        return formatWithPattern(formatStringToDate(date), pattern, locale)
    }

    /**
     * Build a pattern for given style
     * @param style DateTimeStyle
     * @return Pattern
     */
    private fun getPatternForStyle(style: DateTimeStyle): String {
        val pattern: String

        if (style == DateTimeStyle.LONG) {
            pattern = "MMMM dd, yyyy"
        } else if (style == DateTimeStyle.MEDIUM) {
            pattern = "MMM dd, yyyy"
        } else if (style == DateTimeStyle.SHORT) {
            pattern = "MM/dd/yy"
        } else {
            pattern = "EEEE, MMMM dd, yyyy"
        }

        return pattern
    }

    /**
     * Get localized date string
     *
     * @param date Date string
     * @return Formatted localized date string
     */
    @JvmOverloads
    fun formatWithStyle(date: Date?, style: DateTimeStyle, locale: Locale = Locale.getDefault()): String {
        if (date == null && debug) {
            Log.e(TAG, "FormatWithPattern >> Supplied date is null")
        }

        return formatWithPattern(date, getPatternForStyle(style), locale)
    }

    /**
     * Get localized date string (Using default locale)
     *
     * @param date Date string
     * @return Formatted localized date string
     */
    @JvmOverloads
    fun formatWithStyle(date: String, style: DateTimeStyle, locale: Locale = Locale.getDefault()): String {
        return formatWithStyle(formatStringToDate(date), style, locale)
    }

    /**
     * Extract time from date without seconds
     * @see DateTimeFormat.TIME_PATTERN_1
     *
     * @param date Date object
     * @return Time String
     *
     */
    @Deprecated("Use {@link #extractTimeFromDate(Date, boolean)} method instead")
    @JvmOverloads
    fun formatTime(date: Date?, forceShowHours: Boolean = false): String {
        val iso8601Format = SimpleDateFormat(DateTimeFormat.TIME_PATTERN_1, Locale.getDefault())
        iso8601Format.timeZone = TimeZone.getTimeZone(timeZone)
        val time = iso8601Format.format(date)
        val hhmmss = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val hours = Integer.parseInt(hhmmss[0])
        val minutes = Integer.parseInt(hhmmss[1])
        val seconds = Integer.parseInt(hhmmss[2])

        return ((if (hours == 0 && !forceShowHours)
            ""
        else if (hours < 10)
            "0$hours:"
        else
            hours.toString() + ":") +
                (if (minutes == 0)
                    "00"
                else if (minutes < 10)
                    "0$minutes"
                else
                    minutes.toString()) + ":"
                + if (seconds == 0) "00" else if (seconds < 10) "0$seconds" else seconds.toString())
    }

    /**
     * Extract time from date without seconds
     * @param date Date object
     * @return Time string
     *
     */
    @Deprecated("use {@link #extractTimeFromDate(String, boolean)} method instead")
    @JvmOverloads
    fun formatTime(date: String, forceShowHours: Boolean = false): String {
        return formatTime(formatStringToDate(date), forceShowHours)
    }

    /**
     * Extract time from date without seconds
     * @see DateTimeFormat.TIME_PATTERN_1
     *
     * @param date Date object
     * @return Time String
     */
    @JvmOverloads
    fun extractTimeFromDate(date: Date?, forceShowHours: Boolean = false): String {
        val iso8601Format = SimpleDateFormat(DateTimeFormat.TIME_PATTERN_1, Locale.getDefault())
        iso8601Format.timeZone = TimeZone.getTimeZone(timeZone)
        val time = iso8601Format.format(date)
        val hhmmss = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val hours = Integer.parseInt(hhmmss[0])
        val minutes = Integer.parseInt(hhmmss[1])
        val seconds = Integer.parseInt(hhmmss[2])

        return ((if (hours == 0 && !forceShowHours)
            ""
        else if (hours < 10)
            "0$hours:"
        else
            hours.toString() + ":") +
                (if (minutes == 0)
                    "00"
                else if (minutes < 10)
                    "0$minutes"
                else
                    minutes.toString()) + ":"
                + if (seconds == 0) "00" else if (seconds < 10) "0$seconds" else seconds.toString())
    }

    /**
     * Extract time from date without seconds
     * @param date Date object
     * @return Time string
     */
    @JvmOverloads
    fun extractTimeFromDate(date: String, forceShowHours: Boolean = false): String {
        return extractTimeFromDate(formatStringToDate(date), forceShowHours)
    }

    /**
     * Convert millis to human readable time
     *
     * @param millis TimeStamp
     *
     * @return Time String
     */
    fun millisToTime(millis: Long): String {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))

        val hours = TimeUnit.MILLISECONDS.toHours(millis)

        return ((if (hours == 0L)
            ""
        else if (hours < 10)
            "0$hours:"
        else
            hours.toString() + ":") +
                (if (minutes == 0L)
                    "00"
                else if (minutes < 10)
                    "0$minutes"
                else
                    minutes.toString()) + ":"
                + if (seconds == 0L)
            "00"
        else if (seconds < 10)
            "0$seconds"
        else
            seconds.toString())
    }

    /**
     * Convert millis to human readable time
     *
     * @param time Time string
     * @return Time String
     */
    fun timeToMillis(time: String): Long {
        val hhmmss = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var hours = 0
        val minutes: Int
        val seconds: Int

        if (hhmmss.size == 3) {
            hours = Integer.parseInt(hhmmss[0])
            minutes = Integer.parseInt(hhmmss[1])
            seconds = Integer.parseInt(hhmmss[2])
        } else {
            minutes = Integer.parseInt(hhmmss[0])
            seconds = Integer.parseInt(hhmmss[1])
        }

        return ((hours * 60 + minutes * 60 + seconds) * 1000).toLong()
    }

    /**
     * Tell whether or not a given string represent a date time string or a simple date
     *
     * @param dateString Date String
     * @return True if given string is a date time False otherwise
     */
    fun isDateTime(dateString: String?): Boolean {
        return dateString != null && dateString.trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size > 1
    }

    /**
     * Tell whether or not a given date is yesterday
     * @param date Date Object
     * @return True if the date is yesterday False otherwise
     */
    fun isYesterday(date: Date?): Boolean {
        // Check if yesterday
        val c1 = Calendar.getInstance() // today
        c1.add(Calendar.DAY_OF_YEAR, -1) // yesterday
        val c2 = Calendar.getInstance()
        c2.time = date //

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }

    /**
     * Tell whether or not a given date is yesterday
     * @param dateString Date String
     * @return True if the date is yesterday False otherwise
     */
    fun isYesterday(dateString: String): Boolean {
        return isYesterday(formatStringToDate(dateString))
    }

    /**
     * Tell whether or not a given date is today date
     * @param date Date object
     * @return True if date is today False otherwise
     */
    fun isToday(date: Date): Boolean {
        return DateUtils.isToday(date.time)
    }

    /**
     * Tell whether or not a given date is today date
     * @param dateString Date string
     * @return True if date is today False otherwise
     */
    fun isToday(dateString: String): Boolean {
        return isToday(formatStringToDate(dateString)!!)
    }

    /**
     * Get difference between two dates
     *
     * @param nowDate  Current date
     * @param oldDate  Date to compare
     * @param dateDiff Difference Unit
     * @return Difference
     */
    fun getDateDiff(nowDate: Date, oldDate: Date, dateDiff: DateTimeUnits): Int {
        val diffInMs = nowDate.time - oldDate.time
        val days = TimeUnit.MILLISECONDS.toDays(diffInMs).toInt()

        val hours = (TimeUnit.MILLISECONDS.toHours(diffInMs) - TimeUnit.DAYS.toHours(days.toLong())).toInt()

        val minutes = (TimeUnit.MILLISECONDS.toMinutes(diffInMs) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diffInMs))).toInt()

        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMs).toInt()

        when (dateDiff) {
            DateTimeUnits.DAYS -> return days

            DateTimeUnits.SECONDS -> return seconds

            DateTimeUnits.MINUTES -> return minutes

            DateTimeUnits.HOURS -> return hours

            DateTimeUnits.MILLISECONDS -> return diffInMs.toInt()

            else -> return diffInMs.toInt()
        }
    }

    /**
     * Get difference between two dates
     *
     * @param nowDate  Current date
     * @param oldDate  Date to compare
     * @param dateDiff Difference Unit
     * @return Difference
     */
    fun getDateDiff(nowDate: String, oldDate: Date?, dateDiff: DateTimeUnits): Int {
        return getDateDiff(formatStringToDate(nowDate)!!, oldDate!!, dateDiff)
    }

    /**
     * Get difference between two dates
     *
     * @param nowDate  Current date
     * @param oldDate  Date to compare
     * @param dateDiff Difference Unit
     * @return Difference
     */
    fun getDateDiff(nowDate: Date, oldDate: String, dateDiff: DateTimeUnits): Int {
        return getDateDiff(nowDate, formatStringToDate(oldDate)!!, dateDiff)
    }

    /**
     * Get difference between two dates
     *
     * @param nowDate  Current date
     * @param oldDate  Date to compare
     * @param dateDiff Difference Unit
     * @return Difference
     */
    fun getDateDiff(nowDate: String, oldDate: String, dateDiff: DateTimeUnits): Int {
        return getDateDiff(nowDate, formatStringToDate(oldDate), dateDiff)
    }

    /**
     * Get time ago of given date
     *
     * @param context Context
     * @param date    Date object
     * @param style DateTimeStyle
     * @return Time ago string
     */
    @JvmOverloads
    fun getTimeAgo(context: Context, date: Date?, style: DateTimeStyle = DateTimeStyle.AGO_FULL_STRING): String {
        val seconds = getDateDiff(Date(), date!!, DateTimeUnits.SECONDS).toDouble()
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        val phrase: String
        val s: String

        if (seconds <= 0) {
            phrase = context.getString(R.string.time_ago_now)
        } else if (seconds < 45) {
            s = if (style == DateTimeStyle.AGO_FULL_STRING)
                context.getString(R.string.time_ago_full_seconds)
            else
                context.getString(R.string.time_ago_seconds)

            phrase = String.format(s, Math.round(seconds))
        } else if (seconds < 90) {
            s = if (style == DateTimeStyle.AGO_FULL_STRING)
                context.getString(R.string.time_ago_full_minute)
            else
                context.getString(R.string.time_ago_minute)

            phrase = String.format(s, 1)
        } else if (minutes < 45) {
            s = if (style == DateTimeStyle.AGO_FULL_STRING)
                context.getString(R.string.time_ago_full_minutes)
            else
                context.getString(R.string.time_ago_minutes)

            phrase = String.format(s, Math.round(minutes))
        } else if (minutes < 90) {
            s = if (style == DateTimeStyle.AGO_FULL_STRING)
                context.getString(R.string.time_ago_full_hour)
            else
                context.getString(R.string.time_ago_hour)

            phrase = String.format(s, 1)
        } else if (hours < 24) {
            s = if (style == DateTimeStyle.AGO_FULL_STRING)
                context.getString(R.string.time_ago_full_hours)
            else
                context.getString(R.string.time_ago_hours)

            phrase = String.format(s, Math.round(hours))
        } else if (hours < 42) {
            if (isYesterday(date)) {
                phrase = context.getString(R.string.time_ago_yesterday_at, formatTime(date))
            } else {
                phrase = formatWithStyle(date, if (style == DateTimeStyle.AGO_FULL_STRING)
                    DateTimeStyle.FULL
                else
                    DateTimeStyle.SHORT)
            }
        } else if (days < 30) {
            s = if (style == DateTimeStyle.AGO_FULL_STRING)
                context.getString(R.string.time_ago_full_days)
            else
                context.getString(R.string.time_ago_days)

            phrase = String.format(s, Math.round(days))
        } else if (days < 45) {
            s = if (style == DateTimeStyle.AGO_FULL_STRING)
                context.getString(R.string.time_ago_full_month)
            else
                context.getString(R.string.time_ago_month)

            phrase = String.format(s, 1)
        } else if (days < 365) {
            s = if (style == DateTimeStyle.AGO_FULL_STRING)
                context.getString(R.string.time_ago_full_months)
            else
                context.getString(R.string.time_ago_months)

            phrase = String.format(s, Math.round(days / 30))
        } else {
            phrase = formatWithStyle(date, if (style == DateTimeStyle.AGO_FULL_STRING)
                DateTimeStyle.FULL
            else
                DateTimeStyle.SHORT)
        }

        return phrase
    }

    /**
     * Get time ago of given date
     *
     * @param context    Context
     * @param dateString Representing a date time string
     * @return Time ago string
     */
    fun getTimeAgo(context: Context, dateString: String): String {
        return getTimeAgo(context, formatStringToDate(dateString), DateTimeStyle.AGO_FULL_STRING)
    }
}
/**
 * Convert a Java Date object to String
 *
 * @param date Date Object
 * @return Date Object string representation
 */
/**
 * Convert a date string to Java Date Object
 *
 * @param dateString Date String
 * @return Java Date Object
 */
/**
 * Convert a timeStamp into a date considering given timeStamp in milliseconds
 *
 * @see DateTimeUnits.MILLISECONDS
 *
 * @param timeStamp TimeStamp
 * @return Date object
 */
/**
 * Format date using a given pattern
 * apply default locale
 *
 * @param date Date Object
 * @param pattern Pattern
 *
 * @return Formatted date
 */
/**
 * Format date using a given pattern
 * apply default locale
 * @param date Date String
 * @param pattern Pattern
 *
 * @return Formatted date
 */
/**
 * Get localized date string (Using default locale)
 *
 * @param date Date string
 * @return Formatted localized date string
 */
/**
 * Get localized date string (Using default locale)
 *
 * @param date Date string
 * @return Formatted localized date string
 */
/**
 * Extract time from date without seconds
 * @param date Date object
 * @return Time string
 *
 */
/**
 * Extract time from date without seconds
 * @param date Date object
 * @return Time string
 *
 */
/**
 * Extract time from date without seconds
 * @param date Date object
 * @return Time string
 */
/**
 * Extract time from date without seconds
 * @param date Date object
 * @return Time string
 */
/**
 * Get time ago of given date
 *
 * @param context    Context
 * @param date Representing a date time string
 * @return Time ago string
 */
