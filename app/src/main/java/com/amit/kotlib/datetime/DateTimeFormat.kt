package com.amit.kotlib.datetime

/**
 * DateTimeFormat
 * Patterns used to parse given date [DateTimeUtils] will use those pattern
 *
 */
object DateTimeFormat {
    /**
     * Typical MySqL/SQL dateTime format with dash as separator
     */
    val DATE_TIME_PATTERN_1 = "yyyy-MM-dd HH:mm:ss"

    /**
     * Typical MySqL/SQL dateTime format with slash as separator
     */
    val DATE_TIME_PATTERN_2 = "dd/MM/yyyy HH:mm:ss"

    val DATE_TIME_PATTERN_3 = "dd-MM-yyyy HH:mm:ss"

    /**
     * Typical MySqL/SQL date format with dash as separator
     */
    val DATE_PATTERN_1 = "yyyy-MM-dd"

    /**
     * Typical MySqL/SQL date format with slash as separator
     */
    val DATE_PATTERN_2 = "dd/MM/yyyy"

    val DATE_PATTERN_3 = "dd-MM-yyyy"

    /**
     * Time format full in 24 hours format
     */
    val TIME_PATTERN_1 = "HH:mm:ss"

    /**
     * Time format with am/pm in 12 hours format
     */
    val TIME_PATTERN_2 = "hh:mm a"
}
