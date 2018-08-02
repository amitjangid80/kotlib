package com.amit.kotlib.utilities

/*
* Created By AMIT JANGID
* 2018 April 17 - Tuesday - 12:52 PM
**/

object TextUtilities {
    /**
     * replace null method
     * this method will replace null with empty space
     *
     * @param string - string where you want to replace null
     * @return it will return empty string
     */
    fun replaceNull(string: String?): String {
        if (string == null) {
            return ""
        }

        if (string.equals("null", ignoreCase = true)) {
            return ""
        }

        return if (string.equals(" ", ignoreCase = true)) {
            ""
        } else string

    }

    /**
     * replace true or false
     * this method will replace true or false with 1 or 0
     *
     * @param string - string to replace true or false with
     * @return it will return 1 or 0
     */
    fun replaceTrueOrFalse(string: String): Int {
        if (string.equals("True", ignoreCase = true)) {
            return 1
        }

        return if (string.equals("False", ignoreCase = true)) {
            0
        } else 0

    }
}
