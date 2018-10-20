package com.amit.kotlib.utilities

/**
 * Created By AMIT JANGID
 * 2018 April 17 - Tuesday - 12:52 PM
**/
@Suppress("unused", "NAME_SHADOWING")
object TextUtils
{
    /**
     * replace null method
     * this method will replace null with empty space
     *
     * @param string - string where you want to replace null
     * @return it will return empty string
    **/
    fun replaceNullWithEmpty(string: String?): String
    {
        return when
        {
            string == null -> ""
            string.equals("null", ignoreCase = true) -> ""
            string.equals(" ", ignoreCase = true) -> ""
            string.equals("", ignoreCase = true) -> ""
            else -> string
        }
    }

    /**
     * replace true or false
     * this method will replace true or false with 1 or 0
     *
     * @param string - string to replace true or false with
     * @return it will return 1 or 0
    **/
    fun replaceTrueOrFalse(string: String): Int
    {
        return if (string.equals("True", ignoreCase = true)) 1 else 0
    }

    /**
     * 2018 September 14 - Friday - 12:34 PM
     * replace null with zero method
     *
     * this method will replace null or empty values of string with zero
     *
     * @param stringToReplace - string to replace null with
     * @return it will return 1 or 0
    **/
    fun replaceNullWithZero(stringToReplace: String?): Int
    {
        return when
        {
            stringToReplace == null -> 0
            stringToReplace.equals("null", ignoreCase = true) -> 0
            stringToReplace.equals(" ", ignoreCase = true) -> 0
            stringToReplace.equals("", ignoreCase = true) -> 0
            else -> Integer.parseInt(stringToReplace)
        }
    }

    /**
     * 2018 September 14 - Friday - 12:34 PM
     * remove last char method
     *
     * this method will remove the last character of the string
     *
     * @param stringToRemovedLastCharFrom - string to remove the last character from
     * @return it will return string with last character removed
    **/
    fun removeLastChar(stringToRemovedLastCharFrom: String?): String?
    {
        var stringToRemovedLastCharFrom = stringToRemovedLastCharFrom

        if (stringToRemovedLastCharFrom != null && stringToRemovedLastCharFrom.isNotEmpty())
        {
            stringToRemovedLastCharFrom = stringToRemovedLastCharFrom.substring(0, stringToRemovedLastCharFrom.length - 1)
        }

        return stringToRemovedLastCharFrom
    }

    /**
     * capitalizeString method
     *
     * this method will capitalizeString or set the string to upper case
     *
     * @param string - string to capitalize
     * return - will return the string which was passed in capitalize form
    **/
    fun capitalizeString(string: String?): String
    {
        if (string == null || string.isEmpty())
        {
            return ""
        }

        val first = string[0]

        return if (Character.isUpperCase(first))
        {
            string
        }
        else
        {
            Character.toUpperCase(first) + string.substring(1)
        }
    }
}
