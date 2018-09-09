package com.amit.kotlib.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.Settings.Secure
import android.support.v4.util.Pair
import android.util.Log

import com.amit.kotlib.utilities.SharedPreferenceData
import org.json.JSONObject

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/*
 * Created By AMIT JANGID
 * 2018 April 17 - Tuesday - 12:56 PM
 **/

class ApiServices(context: Context)
{
    var mResponseCode = 0
    private val apiPath: String
    private val deviceID: String
    private val sharedPreferenceData: SharedPreferenceData = SharedPreferenceData(context)

    /**
     * get device name method
     *
     *
     * this method will get the name of the device
     */
    val deviceName: String
        get()
        {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL

            return if (model.startsWith(manufacturer))
            {
                capitalizeString(model)
            }
            else
            {
                capitalizeString("$manufacturer $model")
            }
        }

    init
    {
        // set api path before using it
        // this path will consist of the url which is repeated in every api
        // Ex: http://www.example.com/api/
        this.apiPath = sharedPreferenceData.getValue("apiPath")

        // this will get the device id of the device
        this.deviceID = Secure.getString(context.contentResolver, Secure.ANDROID_ID)
    }

    /**
     * capitalizeString method
     *
     * this method will capitalizeString or set the string to upper case
     *
     * @param string - string to capitalize
     * return - will return the string which was passed in capitalize form
     */
    private fun capitalizeString(string: String?): String
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

    /**
     * Make API Call method
     *
     *
     * this method will handle all the api operations like GET OR POST OR PUT
     * parameters for this method are as follows
     *
     *
     * ***********************************************************************************************
     *
     *
     * ** parameter #1
     * ** @param apiName - this parameter will contain the name of the api with some path or without any path
     * - FOR EXAMPLE: with path - MobileAPI/RegisterMobile
     * without path - RegisterMobile
     *
     *
     * ** parameter #2
     * ** @param requestMethod - this parameter will be passed with 3 values
     * #1 - POST OR
     * #2 - PUT OR
     * #3 - GET
     *
     *
     * ** parameter #3
     * ** @param parameters - this parameter will contain parameters which will be passed to the api
     * and required by the api to work properly
     *
     *
     * ** parameter #4
     * ** @param values - this parameter will hold the parameters in JSON format
     * this will be the data which we need to pass along with the api
     * - FOR EXAMPLE: MobileNumber = 9999999999, OTP = 9999, etc.
     *
     *
     * ** parameter #5
     * ** @param hasToken - this parameter should be passed with true or false
     * - this parameter will be used if the api requires some token to work with
     * - if the api requires token then this has to be true
     * - if the api doesn't require token then this has to be false
     *
     * @return String which contains result from API.
     *
     *
     * ***********************************************************************************************
    **/
    fun makeAPICall(apiName: String, requestMethod: String,
                    parameters: Boolean, values: JSONObject,
                    hasToken: Boolean): String?
    {
        try
        {
            val result: String
            val url = URL(apiPath + apiName)

            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = requestMethod
            httpURLConnection.addRequestProperty("Accept", "application/json, text/plain, */*")
            httpURLConnection.addRequestProperty("Content-Type", "application/json; charset=utf-8")
            httpURLConnection.addRequestProperty("IsDeviceMode", "1")

            if (hasToken)
            {
                httpURLConnection.addRequestProperty("x-access-token", sharedPreferenceData.getValue("token"))
                Log.e(TAG, "makeAPICall: x-access-token is: " + sharedPreferenceData.getValue("token"))
            }

            httpURLConnection.connectTimeout = 10000
            httpURLConnection.setRequestProperty("Connection", "close")
            Log.e(TAG, "makeAPICall: api = $url   values = $values")

            if (parameters)
            {
                httpURLConnection.doOutput = true
                val outputStream = DataOutputStream(httpURLConnection.outputStream)
                outputStream.writeBytes(values.toString())
                outputStream.flush()
                outputStream.close()
            }

            val responseCode = httpURLConnection.responseCode
            mResponseCode = responseCode
            Log.e(TAG, "makeAPICall: response code from api is: $mResponseCode")

            if (responseCode == 200)
            {
                val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                val stringBuilder = StringBuilder()
                var line: String? = null

                while ({line = bufferedReader.readLine(); line}() != null)
                {
                    stringBuilder.append(line).append("\n")
                }

                bufferedReader.close()
                result = stringBuilder.toString()
                Log.e(TAG, "makeAPICall: result from server is: $result")
            }
            else
            {
                val responseMessage = httpURLConnection.responseMessage
                Log.e(TAG, "makeAPICall: response message: $responseMessage")

                val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.errorStream))
                val stringBuilder = StringBuilder()
                var line: String? = null

                while ({line = bufferedReader.readLine(); line}() != null)
                {
                    stringBuilder.append(line).append("\n")
                }

                bufferedReader.close()
                result = stringBuilder.toString()
                httpURLConnection.disconnect()
            }

            return result
        }
        catch (e: Exception)
        {
            Log.e(TAG, "makeAPICall: in web api services class:\n")
            e.printStackTrace()
            return null
        }
    }

    /**
     * Get Bitmap Data
     *
     *
     * this method gets bitmap image from the server
     * this can be used to download the image from server or api
     *
     * @param apiPath     - this parameter will contain the apiPath where the image has to be downloaded
     * this apiPath parameter will contain the entire path for the image to be downloaded.
     * @param requestType - request type will be GET OR POST
     * @param parameter   - if any information has to be sent in body then set parameters
     * use json object for this parameter.
     *
     *
     * return           - pair of integer and bitmap value
     * where integer will be the response code
     * and bitmap will be the file uploaded.
     */
    fun getBitmapData(apiPath: String, requestType: String, parameter: String?): Pair<Int, Bitmap>
    {
        var resultVal: Bitmap? = null
        val iStream: InputStream
        var responseCode = 0

        try
        {
            val urlConnect = URL(apiPath)
            val urlConnection = urlConnect.openConnection() as HttpURLConnection

            urlConnection.requestMethod = requestType
            urlConnection.addRequestProperty("Content-Type", "application/json; charset=utf-8")

            Log.e(TAG, "getBitmapData: apiPath for downloading image is: $urlConnect")
            urlConnection.addRequestProperty("x-access-token", sharedPreferenceData.getValue("token"))

            Log.e(TAG, "getBitmapData: x-access-token is: " + sharedPreferenceData.getValue("token"))
            urlConnection.connectTimeout = 30000

            if (parameter != null)
            {
                urlConnection.doOutput = true
                val outputStream = DataOutputStream(urlConnection.outputStream)
                outputStream.writeBytes(parameter)
                outputStream.flush()
                outputStream.close()
            }

            try
            {
                responseCode = urlConnection.responseCode
                Log.e(TAG, "Response Code from api is --  $responseCode")

                if (responseCode == 200)
                {
                    iStream = urlConnection.inputStream

                    /*Creating a bitmap from the stream returned from the apiPath */
                    resultVal = BitmapFactory.decodeStream(iStream)
                    Log.e(TAG, "getBitmapData: result from server is: " + resultVal!!)
                }
                else
                {
                    val str = urlConnection.responseMessage
                    Log.e(TAG, "getBitmapData: response message from api is:$str")
                }
            }
            catch (ex: Exception)
            {
                Log.e(TAG, "getBitmapData: exception while downloading images from api:\n")
                ex.printStackTrace()
            }
            finally
            {
                urlConnection.disconnect()
                Log.e(TAG, "URL in finally is >$apiPath<-->$resultVal")
            }
        }
        catch (e: Exception)
        {
            Log.e(TAG, "Exception while geting bitmap:")
            e.printStackTrace()
            return Pair(responseCode, resultVal)
        }

        return Pair(responseCode, resultVal)
    }

    companion object
    {
        private val TAG = ApiServices::class.java.simpleName
    }
}
