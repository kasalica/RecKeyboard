package com.example.reckeyboard.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.reckeyboard.Database.Row
import com.example.reckeyboard.Database.Row2
import com.example.reckeyboard.Database.Row3
import java.text.DecimalFormat


val DATABASE_NAME = "DB1"
val DATABASE_VERSION = 1
val TABLE1_NAME = "CHAR"
val TABLE2_NAME = "TIME"
val TABLE3_NAME = "ACCURACY"
val TEMPTABLE1_NAME = "TempCHAR"
val TEMPTABLE2_NAME = "TempTIME"
val TEMPTABLE3_NAME = "TempACCURACY"
val COL_ID = "id"
val COL_KEY = "key"
val COL_KEYS = "keys"
val COL_C = "count"
val COL_TIME = "time"
val COL_HOLD_TIME = "hold_time"
val COL_X = "coor_x"
val COL_Y = "coor_y"

class DataBaseHandler(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context,
    DATABASE_NAME, factory,
    DATABASE_VERSION
) {

    private val df: DecimalFormat = DecimalFormat("0.00")

    var row = Row()
    var row2 = Row2()
    var row3 = Row3()
    override fun onCreate(db: SQLiteDatabase?) {

        val createTable1 = "CREATE TABLE " + TABLE1_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_KEY + " VARCHAR(256)," +
                COL_HOLD_TIME + " INTEGER," +
                COL_C + " INTEGER)"

        val createTable2 = "CREATE TABLE " + TABLE2_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_KEYS + " VARCHAR(256)," +
                COL_TIME + " INTEGER," +
                COL_C + " INTEGER)"

        val createTable3 = "CREATE TABLE " + TABLE3_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_KEY + " VARCHAR(256)," +
                COL_X + " REAL," +
                COL_Y + " REAL," +
                COL_C + " INTEGER)"

        val createTempTable1 = "CREATE TABLE " + TEMPTABLE1_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_KEY + " VARCHAR(256)," +
                COL_C + " INTEGER," +
                COL_HOLD_TIME + " INTEGER)"

        val createTempTable2 = "CREATE TABLE " + TEMPTABLE2_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_KEYS + " VARCHAR(256)," +
                COL_TIME + " INTEGER," +
                COL_C + " INTEGER)"

        val createTempTable3 = "CREATE TABLE " + TEMPTABLE3_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_KEY + " VARCHAR(256)," +
                COL_X + " REAL," +
                COL_Y + " REAL," +
                COL_C + " INTEGER)"


        db?.execSQL(createTable1)
        db?.execSQL(createTable2)
        db?.execSQL(createTable3)
        db?.execSQL(createTempTable1)
        db?.execSQL(createTempTable2)
        db?.execSQL(createTempTable3)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun insertData1(character: String, temp_holdTime: Int, temp_count: Int, TableName: String) {

        val db = this.writableDatabase
        val query = "Select * from " + TableName + " WHERE " + COL_KEY + "=?"
        val result = db.rawQuery(query, arrayOf(java.lang.String.valueOf(character)))

        if (result.moveToFirst()) {
            do {
                var count = result.getInt(result.getColumnIndex(COL_C))
                var holdTime = result.getInt(result.getColumnIndex(COL_HOLD_TIME))
                var cv = ContentValues()

                cv.put(COL_C, count + temp_count)
                cv.put(COL_KEY, character)
                cv.put(COL_HOLD_TIME, (holdTime * count + temp_holdTime * temp_count)/ (count + temp_count))

                db.update(
                    TableName, cv, COL_ID + "=? AND " + COL_KEY + "=?",
                    arrayOf(result.getString(result.getColumnIndex(COL_ID)), character)
                )
            } while (result.moveToNext())
        } else {
            val cv = ContentValues()
            cv.put(COL_KEY, character)
            cv.put(COL_C, temp_count)
            cv.put(COL_HOLD_TIME, temp_holdTime)
            db.insert(TableName, null, cv)
        }
        result.close()

    }

    fun insertData2(characters: String, temp_time: Int, temp_count: Int, TableName: String) {

        val db = this.writableDatabase
        val query = "Select * from " + TableName + " WHERE " + COL_KEYS + "=?"
        val result = db.rawQuery(query, arrayOf(java.lang.String.valueOf(characters)))

        if (result.moveToFirst()) {
            do {
                var time = result.getInt(result.getColumnIndex(COL_TIME))
                var count = result.getInt(result.getColumnIndex(COL_C))
                var cv = ContentValues()

                cv.put(COL_TIME, (time * count + temp_time * temp_count)/ (count + temp_count))

                cv.put(
                    COL_C, result.getInt(
                        result.getColumnIndex(
                            COL_C
                        )
                    ) + temp_count
                )
                db.update(
                    TableName, cv,
                    COL_ID + "=? AND " + COL_KEYS + "=?",
                    arrayOf(result.getString(result.getColumnIndex(COL_ID)), characters)
                )

            } while (result.moveToNext())
        } else {
            val cv = ContentValues()
            cv.put(COL_C, temp_count)
            cv.put(COL_KEYS, characters)
            cv.put(COL_TIME, temp_time)
            db.insert(TableName, null, cv)
        }
        result.close()
    }


    fun insertData3(character: String, temp_x: Float, temp_y: Float, temp_count: Int, TableName: String) {

        val db = this.writableDatabase
        val query = "Select * from " + TableName + " WHERE " + COL_KEY + "=?"
        val result = db.rawQuery(query, arrayOf(java.lang.String.valueOf(character)))
        if (result.moveToFirst()) {
            do {
                var x = result.getFloat(result.getColumnIndex(COL_X))
                var y = result.getFloat(result.getColumnIndex(COL_Y))
                var count = result.getInt(result.getColumnIndex(COL_C)).toFloat()

                var cv = ContentValues()
                cv.put(COL_X, df.format((x * count + temp_x * temp_count) / (count + temp_count)))
                cv.put(COL_Y, df.format((y * count + temp_y * temp_count) / (count + temp_count)))
                cv.put(COL_C, count + temp_count
                )

                db.update(
                    TableName, cv, COL_ID + "=? AND " + COL_KEY + "=?",
                    arrayOf(result.getString(result.getColumnIndex(COL_ID)), character)
                )
            } while (result.moveToNext())
        } else {
            val cv = ContentValues()
            cv.put(COL_KEY, character)
            cv.put(COL_C, temp_count)
            cv.put(COL_X, df.format(temp_x))
            cv.put(COL_Y, df.format(temp_y))
            db.insert(TableName, null, cv)
        }
        result.close()
    }

    fun readData1(TableName: String): MutableList<Row> {

        var list: MutableList<Row> = ArrayList()
        val db = this.readableDatabase

        val query = "Select * from " + TableName + " ORDER BY " + COL_KEY

        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var row = Row()
                row.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                row.character = result.getString(result.getColumnIndex(COL_KEY))
                row.holdTime = result.getString(result.getColumnIndex(COL_HOLD_TIME)).toInt()
                row.count = result.getString(result.getColumnIndex(COL_C)).toInt()
                list.add(row)
            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }

    fun readData2(TableName: String): MutableList<Row2> {

        var list: MutableList<Row2> = ArrayList()
        val db = this.readableDatabase

        val query = "Select * from " + TableName + " ORDER BY " + COL_KEYS

        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var row = Row2()
                row.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                row.characters = result.getString(result.getColumnIndex(COL_KEYS))
                row.time = result.getString(result.getColumnIndex(COL_TIME)).toInt()
                row.count = result.getString(result.getColumnIndex(COL_C)).toInt()
                list.add(row)
            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }

    fun readData3(TableName: String): MutableList<Row3> {

        var list: MutableList<Row3> = ArrayList()
        val db = this.readableDatabase

        val query = "Select * from " + TableName + " ORDER BY " + COL_KEY

        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var row = Row3()
                row.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                row.character = result.getString(result.getColumnIndex(COL_KEY))
                row.x = result.getString(result.getColumnIndex(COL_X)).toFloat()
                row.y = result.getString(result.getColumnIndex(COL_Y)).toFloat()
                row.count = result.getString(result.getColumnIndex(COL_C)).toInt()
                list.add(row)

            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }


    fun deleteData() {
        val db = this.writableDatabase
        db.delete(TABLE1_NAME, null, null)
        db.delete(TABLE2_NAME, null, null)
        db.delete(TABLE3_NAME, null, null)
        db.close()
    }

    fun deleteTempData() {
        val db = this.writableDatabase
        db.delete(TEMPTABLE1_NAME, null, null)
        db.delete(TEMPTABLE2_NAME, null, null)
        db.delete(TEMPTABLE3_NAME, null, null)
        db.close()
    }

    fun transferTempData(){

        var db = this.readableDatabase

        var query = "Select * from " + TEMPTABLE1_NAME
        val query2 = "Select * from " + TEMPTABLE2_NAME
        val query3 = "Select * from " + TEMPTABLE3_NAME

        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                row.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                row.character = result.getString(result.getColumnIndex(COL_KEY))
                row.count = result.getString(result.getColumnIndex(COL_C)).toInt()
                row.holdTime = result.getInt(result.getColumnIndex(COL_HOLD_TIME))
                Log.e("HOLDDDDDD TIMEEEEE: " , row.holdTime.toString())
                insertData1(row.character, row.holdTime, row.count, TABLE1_NAME)
            } while (result.moveToNext())
        }
        result.close()

        val result2 = db.rawQuery(query2, null)
        if (result2.moveToFirst()) {
            do {
                row2.id = result2.getString(result2.getColumnIndex(COL_ID)).toInt()
                row2.characters = result2.getString(result2.getColumnIndex(COL_KEYS))
                row2.time = result2.getString(result2.getColumnIndex(COL_TIME)).toInt()
                row2.count = result2.getString(result2.getColumnIndex(COL_C)).toInt()

                insertData2(row2.characters, row2.time, row2.count, TABLE2_NAME)
            } while (result2.moveToNext())
        }
        result2.close()

        val result3 = db.rawQuery(query3, null)
        if (result3.moveToFirst()) {
            do {
                row3.id = result3.getString(result3.getColumnIndex(COL_ID)).toInt()
                row3.character = result3.getString(result3.getColumnIndex(COL_KEY))
                row3.x = result3.getString(result3.getColumnIndex(COL_X)).toFloat()
                row3.y = result3.getString(result3.getColumnIndex(COL_Y)).toFloat()
                row3.count = result3.getString(result3.getColumnIndex(COL_C)).toInt()

                insertData3(row3.character, row3.x, row3.y, row3.count, TABLE3_NAME)
            } while (result3.moveToNext())
        }
        result3.close()
    }
}
