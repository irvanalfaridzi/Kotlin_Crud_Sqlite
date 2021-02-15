package com.example.menumakananminuman
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast
class MenuDbManager(context: Context) {
    private val dbName = "JSANotes"
    private val dbTable = "Notes"
    private val colId = "Id"
    private val colNama = "Nama"
    private val colHarga = "Harga"
    private val colGambar = "Gambar"
    private val dbVersion = 1

    private val CREATE_TABLE_SQL =
        "CREATE TABLE IF NOT EXISTS $dbTable ($colId INTEGER PRIMARY KEY,$colNama TEXT, $colHarga DOUBLE, $colGambar TEXT);"
    private var db: SQLiteDatabase? = null

    init {
        var dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
    }

    fun insert(values: ContentValues): Long {

        val ID = db!!.insert(dbTable, "", values)
        return ID
    }

    fun queryAll(): Cursor {

        return db!!.rawQuery("select * from $dbTable", null)
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {

        val count = db!!.delete(dbTable, selection, selectionArgs)
        return count
    }

    fun update(values: ContentValues, selection: String, selectionargs: Array<String>): Int {

        val count = db!!.update(dbTable, values, selection, selectionargs)
        return count
    }

    fun query(projection: Array<String>, selection: String, selectionArgs: Array<String>, sorOrder: String): Cursor {

        val qb = SQLiteQueryBuilder()
        qb.tables = dbTable
        val cursor = qb.query(db, projection, selection, selectionArgs, null, null, sorOrder)
        return cursor
    }

    inner class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, dbName, null, dbVersion) {

        var context: Context? = context

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(CREATE_TABLE_SQL)
            Toast.makeText(this.context, " database is created", Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table IF EXISTS $dbTable")
        }
    }
}