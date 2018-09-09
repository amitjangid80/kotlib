package com.amit.kotlib.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Created By AMIT JANGID
 * 2018 April 17 - Tuesday - 12:10 PM
 * Database class for creating database for the application111
**/

class Database private constructor(context: Context, databaseName: String) :
        SQLiteOpenHelper(context, databaseName, null, databaseVersion)
{
    override fun onCreate(db: SQLiteDatabase)
    {

    }

    /**
     * 2018 April 17 - Tuesday - 12:11 PM
     * Get Record Count
     *
     * this method will get the count of the record in that table
     * with single record count or count of all records
     *
     * @param query - THIS QUERY WILL CONTAIN A SELECT QUERY WITH OR WITHOUT CONDITION
     * Ex: SELECT * FROM TABLE_NAME
     * OR
     * SELECT * FROM TABLE_NAME WHERE ID = 1
    **/
    fun getRecordCount(query: String): Int
    {
        try
        {
            val count: Int
            val cursor = readableDatabase.rawQuery(query, null)

            if (cursor != null)
            {
                cursor.moveToFirst()
                count = cursor.count
                cursor.close()
                return count
            }
            else
            {
                Log.e(TAG, "getRecordCount: cursor was null for query: $query")
                return 0
            }
        }
        catch (e: Exception)
        {
            Log.e(TAG, "getRecordCount: while getting count of record in database file:\n")
            e.printStackTrace()
            return 0
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)
    {

    }

    companion object
    {
        private val TAG = Database::class.java.simpleName
        private val databaseVersion = 1
        private var mDatabase: Database? = null

        internal fun getDBInstance(context: Context, databaseName: String): Database
        {
            if (mDatabase == null)
            {
                synchronized(Database::class.java)
                {
                    if (mDatabase == null)
                    {
                        mDatabase = Database(context, databaseName)
                    }
                }
            }

            return mDatabase!!
        }
    }
}
