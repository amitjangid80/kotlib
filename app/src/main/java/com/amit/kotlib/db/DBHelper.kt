package com.amit.kotlib.db

import android.content.Context
import android.database.Cursor
import android.util.Log
import com.amit.kotlib.utilities.SharedPreferenceData
import java.util.*

/**
 * Created by AMIT JANGID
 * 2018 Feb 01 - Thursday - 02:55 PM
 *
 * this class has method for executing db queries
 * like: creating table, inserting into table, deleting table, dropping table
**/
@Suppress("unused")
class DBHelper
/**
 * Constructor of the class
 * you have to set the db name first before using this class.
 *
 * @param context - context
**/
(context: Context)
{
    private val db: Database

    init
    {
        val sharedPreferenceData = SharedPreferenceData(context)
        db = Database.getDBInstance(context, sharedPreferenceData.getValue("dbName"))
    }

    // region COMMENTS OF EXECUTE DATABASE OPERATIONS METHOD

    /**
     * Created By AMIT JANGID
     * 2018 Feb 01 - Thursday - 02:57 PM
     * Execute Database Operations - method name
     *
     * This method is an universal method
     * which will perform all the operations of database
     * like - create, delete, insert, update,
     *
     * parameters to be passed in this method are as follows:
     * the parameters below are numbered and has to passed in the same sequence
     *
     * parameter #1:
     * @param tableName - This will be the name of the table on which the operations has to be performed
     * - FOR EXAMPLE: tableName = "User"
     *
     * parameter #2:
     * @param values - This parameter is an object of LinkedHashMap
     * - this parameter will contain set of Key and Value
     * to create table or insert data or update we will have to pass this parameter with data
     * - FOR EXAMPLE: - values.put("Name", "'Amit'") - this for inserting data and updating data
     * - values.put("Name", "TEXT") - this for creating table
     *
     * parameter #3
     * @param operations - Details as follows
     *
     * the values to the parameter operations are as follows:
     *
     * c - for creating new table
     * - while doing create operation
     * the user has to pass the data type also with the field of the table
     * FOR EXAMPLE: values.put("Name", "TEXT") - this for creating table
     *
     * d - for deleting values from table
     * - FOR DELETING SINGLE RECORD OR ROW
     * - the user has to pass true for the parameter "hasConditions"
     * because delete operation cannot be performed without where clause or condition
     * so the user has to pass data to "conditionalValues" parameter
     * - FOR EXAMPLE: hasConditions = true then conditionalValues.put("ID", "'1'")
     *
     * - FOR DELETING ALL THE RECORDS OR ROWS
     * - the user has to pass null of values,
     * false for hasConditions and null of conditionalValues
     * - FOR EXAMPLE: values = null, hasConditions = false, conditionalValues = null
     *
     * dr - for dropping the table
     *
     * i - for inserting values in the table
     * - while doing insert operation
     * - the values that the user has to pass is of LinkedHashMap object
     * read the comments of values parameter for how to pass the values
     * - the user has to pass the field name and following with the value of the field
     * if the field is of type string or text,
     * then the value should be in single quotes ('')
     * if the field is of type integer,
     * then the user can pass the value without the quotes or with quotes
     * - FOR EXAMPLE: values.put("firstName", "'Amit'"),
     * values.put("active", "1") or
     * values.put("active", "'1'") - better option this way
     *
     * u - for updating values of table
     * - while performing update operation
     * - pass values with field name of the table and following with values
     * - pass hasConditions value as true and also pass conditionalValues with data
     * - FOR EXAMPLE: hasConditions = true and conditionalValue.put("ID", "1") OR
     * conditionalValues("ID", "'1'") - this option is better
     * values.put("firstName", "'Amit'"),
     * values.put("email", "'anythiing@gmail.com'")
     *
     *
     *
     * parameter #4
     * @param hasConditions - This parameter is used when the user has to perform Update or Delete operations
     * - pass true when doing UPDATE OR DELETE operations
     * and pass false if not doing UPDATE OR DELETE operations
     * for delete operation false can be passed,
     * if entire data of the table is to be removed
     * - when passing this parameter with true,
     * then you have to pass conditionalValues parameter as well
     *
     * parameter #5
     * @param conditionalValues - This parameter is used when the hasConditions parameter is set to true:
     * - If the hasConditions parameter is true
     * then conditionalValues should have an Object of LinkedHashMap
     * - FOR EXAMPLE: conditionalValues.put("ID", "'1'") - for updating data at this "ID"
     * - If the hasConditions parameter is false
     * then conditionalValues can be null
     *
     * @return true or false
    **/

    // endregion
    fun executeDatabaseOperations(tableName: String,
                                  operations: String,
                                  values: LinkedHashMap<String, String>?,
                                  hasConditions: Boolean,
                                  conditionalValues: LinkedHashMap<String, String>?): Boolean
    {
        try
        {
            var query = ""

            when (operations.toLowerCase())
            {
                // this case will perform create table operations
                // and this case will generate create table query
                "c" ->

                    // in create query values array list cannot be null
                    // checking if values array list is not null
                    // if not null then converting the array list into string
                    // after converting, replacing square brackets in the string
                    // and then passing the string to query string for db operation
                    if (values != null)
                    {
                        Log.e(TAG, "executeDatabaseOperations: map of values for create query is: " + values.toString())

                        var strValues = values.toString()
                        strValues = strValues.replace("{", "")
                        strValues = strValues.replace("}", "")
                        strValues = strValues.replace("=", " ")

                        query = "CREATE TABLE IF NOT EXISTS $tableName ($strValues)"
                        Log.e(TAG, "executeDatabaseOperations: create query: $query")
                    }
                    else
                    {
                        Log.e(TAG, "executeDatabaseOperations: Values was null for creating table in.....")
                        // return "Values was null for creating table.....";
                        return false
                        // return null;
                    }

                // this case will perform delete operation
                // and this case will generate delete query
                "d" ->

                    // while performing delete where clause is compulsory if deleting single record
                    // -- if you want to delete single row then values can be null,
                    //    hasCondition has to be true and conditional values cannot be null
                    // -- if you want to delete all the records of the table
                    //    then just pass the table name and operation as "d"
                    //    rest of the parameters like values can be null,
                    //    has condition can be false and conditional values can be false
                    // checking if has conditions is set to true or not
                    // if yes then checking if conditional values array list is not null
                    // if not null then converting the conditional values array list to string
                    // after converting, replacing the square brackets in the string
                    // and then passing the query string for delete operations
                    if (hasConditions)
                    {
                        // checking if conditional values array list if not null
                        if (conditionalValues != null)
                        {
                            var strConditionalValues = conditionalValues.toString()
                            strConditionalValues = strConditionalValues.replace("{", "")
                            strConditionalValues = strConditionalValues.replace("}", "")

                            query = "DELETE FROM $tableName WHERE $strConditionalValues"
                            Log.e(TAG, "executeDatabaseOperations: delete query: $query")
                        }
                        else
                        {
                            Log.e(TAG, "executeDatabaseOperations: Conditional values was null for Delete query.....")
                            // return "Conditional values was null for Delete query.....";
                            return false
                            // return null;
                        }
                    }
                    else
                    {
                        query = "DELETE FROM $tableName"
                        Log.e(TAG, "executeDatabaseOperations: delete query: $query")

                        // Log.e(TAG, "executeDatabaseOperations: False passed for has conditions parameter while performing delete query.....");
                        // return "False passed for has conditions parameter while performing delete query.....";
                        // return false;
                        // return null;
                    }

                // this case will perform drop operation
                // and this case will generate drop query
                "dr" ->
                {

                    // this query will drop the table if and only if the table exists
                    query = "DROP TABLE IF EXISTS '$tableName'"
                    Log.e(TAG, "executeDatabaseOperations: drop query: $query")
                }

                // this case will perform insert operation
                // and this case will generate insert query
                "i" ->

                    // in insert query the values array list cannot be null
                    // checking if values array list is not null
                    // if not null then converting values array list to string
                    // after converting, replacing the square brackets in the string
                    // then passing the string to query string
                    if (values != null)
                    {
                        Log.e(TAG, "executeDatabaseOperations: map of values for insert query is: " + values.toString())
                        val strValuesList = ArrayList<String>()

                        for (k in values.keys)
                        {
                            values[k]?.let { strValuesList.add(it) }
                        }

                        var fields = values.keys.toString()
                        fields = fields.replace("[", "")
                        fields = fields.replace("]", "")

                        var strValues = strValuesList.toString()
                        strValues = strValues.replace("[", "")
                        strValues = strValues.replace("]", "")

                        query = "INSERT INTO $tableName ($fields) VALUES ($strValues)"
                        Log.e(TAG, "executeDatabaseOperations: insert query: $query")
                    }
                    else
                    {
                        Log.e(TAG, "executeDatabaseOperations: Values was null while inserting data in table.....")
                        // return "Values was null while inserting data in table.....";
                        return false
                        // return null;
                    }

                "u" ->

                    // while performing update operation has condition value should be true
                    // checking if has conditions is set to true
                    // if yes then checking values and conditional values array list are not null
                    // if not null then converting them to strings
                    // after converting, replacing square brackets in the string
                    // then passing the string to query string
                    if (hasConditions)
                    {
                        if (values != null && conditionalValues != null)
                        {
                            var strValues = values.toString()
                            strValues = strValues.replace("{", "")
                            strValues = strValues.replace("}", "")

                            var strConditionalValues = conditionalValues.toString()
                            strConditionalValues = strConditionalValues.replace("{", "")
                            strConditionalValues = strConditionalValues.replace("}", "")

                            query = "UPDATE $tableName SET $strValues WHERE $strConditionalValues"
                            Log.e(TAG, "executeDatabaseOperations: update query: $query")
                        }
                        else
                        {
                            Log.e(TAG, "executeDatabaseOperations: Values or Conditional values was null for update query.....")
                            // return "Values or Conditional values was null for update query.....";
                            return false
                            // return null;
                        }
                    }
                    else
                    {
                        Log.e(TAG, "executeDatabaseOperations: False passed for has conditions parameter while performing update query.....")
                        // return "False passed for has conditions parameter while performing update query.....";
                        return false
                        // return null;
                    }
            }

            // executing the query generated in above cases
            // if successful then it will return true
            // return true;
            db.writableDatabase.execSQL(query)
            return true
        }
        catch (e: Exception)
        {
            Log.e(TAG, "executeDatabaseOperations: in database helper class:\n")
            e.printStackTrace()
            return false
        }
    }

    // region COMMENTS FOR executeQuery method

    /**
     * 2018 Feb 01 - Thursday - 03:52 PM
     * Execute Select Query
     *
     * parameters for this method are
     *
     * @param query - query that you want to execute
     *
     * @return cursor with records from the table
    **/

    // endregion COMMENTS FOR executeQuery method
    fun executeSelectQuery(query: String): Cursor?
    {
        try
        {
            // query execution
            val cursor = db.writableDatabase.rawQuery(query, null)

            // if cursor is not null then moving the position to first
            // and returning the cursor
            if (cursor != null)
            {
                cursor.moveToFirst()
            }
            else
            {
                Log.e(TAG, "executeSelectQuery: cursor was null. No data found.")
                return null
            }

            return cursor
        }
        catch (e: Exception)
        {
            Log.e(TAG, "executeSelectQuery: in database helper class:\n")
            e.printStackTrace()
            return null
        }
    }

    // region COMMENTS FOR executeQuery method

    /**
     * 2018 Feb 01 - Thursday - 03:52 PM
     * Execute Select Query
     *
     * parameters for this method are
     *
     * @param tableName - name of the table to perform select operation
     *
     * @param values - values to perform select query on
     *
     * @param hasConditions - if you want to use the where clause in select query
     * then this parameter should be set to true
     * else this parameter can be false
     *
     * @param conditionalValues - if the hasConditions is set to true
     * then the user has to pass conditionalValues
     * else it can be null
     *
     * the below lines are not in use so ignore it
     * s - for selecting values from table
     * - pass * in values parameter when doing select operations
     * when you want to select every thing from the table
     * no matter condition is there or not
     * - pass values parameters with the name of the columns in the table
     * when you want to select one or multiple columns from the table
     * no matter condition is there or not
    **/

    // endregion COMMENTS FOR executeQuery method
    fun executeSelectQuery(tableName: String,
                           values: String?,
                           hasConditions: Boolean,
                           conditionalValues: StringBuilder?): Cursor?
    {
        try
        {
            val cursor: Cursor?

            if (values != null)
            {
                val query: String

                // check if has condition is tru
                // if yes the conditional values should not be null
                if (hasConditions)
                {
                    // check ig conditional values is passed
                    // it should be of string builder type
                    // where user has to pass values to be passed in where clause
                    //
                    // FOR EX: firstName = 'FirstNameValue' OR
                    //         firstName LIKE %Term to be searched%
                    if (conditionalValues != null)
                    {
                        // building conditional query
                        query = "SELECT " + values + " FROM " + tableName + " WHERE " + conditionalValues.toString() + ""
                        Log.e(TAG, "executeSelectQuery: Select query with conditions is: $query")
                    }
                    else
                    {
                        Log.e(TAG, "executeSelectQuery: conditional values is null.")
                        return null
                    }
                }
                else
                {
                    // building non conditional values
                    query = "SELECT $values FROM $tableName"
                    Log.e(TAG, "executeSelectQuery: Select query without conditions is: $query")
                }

                // executing query
                cursor = db.writableDatabase.rawQuery(query, null)

                // if cursor is not null then moving the position to first
                // and returning the cursor
                if (cursor != null) {

                    cursor.moveToFirst()
                }
                else
                {
                    Log.e(TAG, "executeSelectQuery: cursor was null. No data found.")
                    return null
                }
            }
            else
            {
                Log.e(TAG, "executeSelectQuery: values was null for select query.")
                return null
            }

            return cursor
        }
        catch (e: Exception)
        {
            Log.e(TAG, "executeSelectQuery: in database helper class:\n")
            e.printStackTrace()
            return null
        }
    }

    // region COMMENTS FOR getRecordCount method

    /**
     * 2018 Feb 02 - Friday - 01:36 PM
     * Get Record Count
     *
     * this method gets the count of the records in the table
     *
     * parameters to be passed are as follows
     * *********************************************************************************************
     *
     * @param tableName - pass the name of the table on which you have to perform the operation
     *
     * @param values - pass either * or just a single name of the field of that table
     *
     * @param hasConditions - if you want to get the count of a single record with some conditions
     * then set this as true else it will be false.
     *
     * if this parameter is set to true then
     * conditional values has to be provided else it won't work.
     *
     * @param conditionalValues - pass conditional values in this liked hash map
     * it can be null if hasConditions is set to false
     * if hasConditions is set to true then this param
     * has to be passed.
     *
     * *********************************************************************************************
     *
     * @return this method will return the count of the record in the table
    **/

    // endregion COMMENTS FOR getRecordCount method
    fun getRecordCount(tableName: String,
                       values: String,
                       hasConditions: Boolean,
                       conditionalValues: StringBuilder?): Int
    {
        try
        {
            val query: String

            // check if has condition is true
            // if yes then conditional values should be passed
            if (hasConditions)
            {
                // checking if conditional values is not null
                // if yes then then building query with conditions
                if (conditionalValues != null)
                {
                    // building conditional query
                    query = "SELECT " + values + " FROM " + tableName + " WHERE " + conditionalValues.toString() + ""
                    Log.e(TAG, "getRecordCount: query with condition is: $query")
                }
                else
                {
                    // building non conditional query
                    Log.e(TAG, "getRecordCount: conditional value was null.")
                    return 0
                }
            }
            else
            {
                query = "SELECT $values FROM $tableName"
                Log.e(TAG, "getRecordCount: query without condition is: $query")
            }

            return if (!query.equals("", ignoreCase = true))
            {
                db.getRecordCount(query)
            }
            else
            {
                Log.e(TAG, "getRecordCount: query was not generated.")
                0
            }
        }
        catch (e: Exception)
        {
            Log.e(TAG, "getRecordCount: in database helper class:\n")
            e.printStackTrace()
            return 0
        }
    }

    /**
     * 2018 September 07 - Friday - 05:39 PM
     * is table exists method
     *
     * this method will check if a table exists in database
     * if yes is will not execute create table query
     * else it will execute
     *
     * @param tableName - name of the table to check if that table exists or not
     *
     * @return true - if table exists in database
     * false - if table not exists in database
    **/
    fun isTableExists(tableName: String): Boolean
    {
        try
        {
            // query for checking if table exists in database
            val query = "SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '$tableName'"

            // executing the query using cursor
            val cursor = db.readableDatabase.rawQuery(query, null)

            // checking if cursor is not null
            if (cursor != null)
            {
                // cursor is not null, moving the cursor position to first
                cursor.moveToFirst()

                // getting the count from cursor
                val count = cursor.count

                // closing the cursor
                cursor.close()

                // returning true if table exists in database
                // else false if table does not exists in database
                return count > 0
            }
            else
            {
                return false
            }
        }
        catch (e: Exception)
        {
            Log.e(TAG, "isTableExists: exception in is table exists method:\n")
            e.printStackTrace()
            return false
        }
    }

    /**
     * 2018 August 13 - Monday - 12:34 PM
     * get max field method
     *
     * this method will get max value of the field in the table
     *
     * @param field - primary key of the table to get the max value or
     * an integer field of the table to get the max value
     *
     * @param tableName - table name from where you need to get max value
     *
     * @return - max value of the field passed
    **/
    fun getMaxId(field: String?, tableName: String): Int
    {
        try
        {
            if (field != null && field.isNotEmpty())
            {
                if (tableName.isEmpty())
                {
                    Log.e(TAG, "getMaxId: table name is required which was not passed")
                    return 0
                }

                val query = "SELECT MAX($field) AS ID FROM $tableName"
                val cursor = db.readableDatabase.rawQuery(query, null)

                return if (cursor != null)
                {
                    cursor.moveToFirst()
                    val id = cursor.getInt(cursor.getColumnIndex("ID"))

                    cursor.close()
                    id
                }
                else
                {
                    0
                }
            }
            else
            {
                return 0
            }
        }
        catch (e: Exception)
        {
            Log.e(TAG, "getMaxId: exception while getting max field from table:\n")
            e.printStackTrace()
            return 0
        }
    }

    /**
     * 2018 August 20 - Monday - 04:01 PM
     * execute query method
     *
     * this method is used to execute a query
     * this method will return true if the query is executed successfully
     *
     * @param query - query that you want to execute without getting any particular result.
     *
     * @return - true if query was successful
     * false if query was not successful.
    **/
    fun executeQuery(query: String?): Boolean
    {
        try
        {
            return if (query != null && !query.equals("", ignoreCase = true))
            {
                db.writableDatabase.execSQL(query)
                true
            }
            else
            {
                false
            }
        }
        catch (ex: Exception)
        {
            Log.e(TAG, "executeQuery: exception while executing query:\n")
            ex.printStackTrace()
            return false
        }
    }

    companion object
    {
        private val TAG = DBHelper::class.java.simpleName
    }
}
