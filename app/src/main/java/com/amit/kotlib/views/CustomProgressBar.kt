@file:Suppress("DEPRECATION")

package com.amit.kotlib.views

import android.app.ProgressDialog
import android.content.Context
import android.os.Handler

/**
 * Created by AMIT JANGID on 17-Sep-18.
 *
 * this class is an progress dialog class which will show progress
 * and text while performing any long operations
 *
 * EX: making many api calls at once.
**/
@Suppress("unused")
class CustomProgressBar(private val mContext: Context)
{
    private var currentProgress = 0
    private var progressBarMaxSize = 100

    private var titleMessage: String? = null
    private var updateMessage: String? = null

    private var cancelable = true
    private var progressDialog: ProgressDialog? = null

    var handler = Handler(Handler.Callback { msg ->
        when (msg.what)
        {
            1 ->
            {

                progressDialog = ProgressDialog(mContext)
                progressDialog!!.setTitle(titleMessage)
                progressDialog!!.setMessage(updateMessage)
                progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                progressDialog!!.setCancelable(true)
                progressDialog!!.progress = currentProgress
                progressDialog!!.max = progressBarMaxSize
                progressDialog!!.show()
            }

            2 ->
            {

                progressDialog!!.setTitle(titleMessage)
                progressDialog!!.setMessage(updateMessage)
                progressDialog!!.progress = currentProgress
                progressDialog!!.max = progressBarMaxSize
                progressDialog!!.setCancelable(cancelable)
            }

            3 ->
            {
                progressDialog!!.dismiss()
            }

            4 ->
            {

            }
        }

        false
    })

    fun setProgressBarMaxSize(progressBarMaxSize: Int)
    {
        this.progressBarMaxSize = progressBarMaxSize
    }

    fun setCurrentProgress(currentProgress: Int)
    {
        this.currentProgress = currentProgress
    }

    fun setUpdateMessage(updateMessage: String)
    {
        this.updateMessage = updateMessage
    }

    fun setTitleMessage(titleMessage: String)
    {
        this.titleMessage = titleMessage
    }

    fun setCancelable(cancelable: Boolean)
    {
        this.cancelable = cancelable
    }
}
