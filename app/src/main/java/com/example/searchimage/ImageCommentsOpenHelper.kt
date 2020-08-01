package com.example.searchimage

import android.R.id
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class ImageCommentsOpenHelper(
    context: Context,
    factory: SQLiteDatabase.CursorFactory?
) :
    SQLiteOpenHelper(
        context, DATABASE_NAME,
        factory, DATABASE_VERSION
    ) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME
                + " TEXT," + COLUMN_IMAGE_ID
                + " TEXT" + ")")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addComments(data: Data, str: String) {
        val values = ContentValues()
        values.put(COLUMN_NAME, str)
        val db = this.writableDatabase

        val rows = db.update(TABLE_NAME, values, COLUMN_IMAGE_ID + "=?", arrayOf(data.iD))
        if (rows == 0) {
            values.put(COLUMN_IMAGE_ID, data.iD)
            db.insert(TABLE_NAME, null, values)
        }

        db.close()
    }

    fun getCommnets(data: Data): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT $COLUMN_NAME FROM $TABLE_NAME where $COLUMN_IMAGE_ID =?",
            arrayOf(data.iD)
        )
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "imagesearch.db"
        val TABLE_NAME = "comments"
        val COLUMN_ID = "_id"
        val COLUMN_IMAGE_ID = "image_id"
        val COLUMN_NAME = "user_comments"
    }
}