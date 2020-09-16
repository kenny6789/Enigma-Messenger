package com.hotapp229.messenger

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.show_recent_text.view.*
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DBShowRecentMessage(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "ShowRecentMessagesDatabase"
        private val TABLE_RECENT_MESSAGE = "RecentMessageTable"
        private val KEY_ID = "id"
        private val KEY_MESSAGE = "message"
        private val KEY_TIME = "time"
        private val KEY_READSTATUS = "status"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_RECENT_MESSAGE = ("CREATE TABLE $TABLE_RECENT_MESSAGE($KEY_ID STRING PRIMARY KEY,$KEY_MESSAGE TEXT,$KEY_TIME STRING, $KEY_READSTATUS STRING)")
       // val CREATE_TABLE_INCOMING_MESSAGES = ("CREATE TABLE IF NOT EXISTS $tableName($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_MESSAGE STRING, $KEY_TIME STRING, $KEY_SOURCE STRING)")
        db?.execSQL(CREATE_TABLE_RECENT_MESSAGE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


    fun createDatabase(tableName: String, message: String, date: String, source: String){
        val db: SQLiteDatabase? = null
        val KEY_ID = "id"
        val KEY_MESSAGE = "message"
        val KEY_TIME = "time"
        val KEY_SOURCE = "source"
        val CREATE_TABLE_INCOMING_MESSAGES = ("CREATE TABLE IF NOT EXISTS $tableName($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_MESSAGE STRING, $KEY_TIME STRING, $KEY_SOURCE STRING)")
        db?.execSQL(CREATE_TABLE_INCOMING_MESSAGES)

        val dbSQL = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_MESSAGE, message)
        contentValues.put(KEY_TIME, date)
        contentValues.put(KEY_SOURCE, source)

        dbSQL.insert(tableName, null, contentValues)
        dbSQL.close()
    }

    fun saveMessage(phoneNumber: String, text: String, date: String, status: String){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, phoneNumber)
        contentValues.put(KEY_MESSAGE, text)
        contentValues.put(KEY_TIME,date)
        contentValues.put(KEY_READSTATUS, status)

        var success = db.insert(TABLE_RECENT_MESSAGE, null, contentValues)
        if(success < 0){
            db.update(TABLE_RECENT_MESSAGE,contentValues, "id=$phoneNumber",null)
        }
        db.close()
        //return success
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun viewMessage(recyclerView: RecyclerView, context: Context) {
        val adapter = GroupAdapter<GroupieViewHolder>()
        //val returnedPhoneNumber: Number? = null
        val selectQuery = "SELECT * FROM $TABLE_RECENT_MESSAGE"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLException){
            db.execSQL(selectQuery)
        }
        var phoneNumber: String? = null
        var message: String
        var date: String
        var readStatus: String
        if (cursor != null) {
            if(cursor.moveToFirst()){
                do{
                    phoneNumber = cursor.getString(cursor.getColumnIndex("id"))
                    message = cursor.getString(cursor.getColumnIndex("message"))
                    date = cursor.getString(cursor.getColumnIndex("time"))
                    readStatus = cursor.getString(cursor.getColumnIndex("status"))
                    adapter.add(MessageInfo(message, phoneNumber, date))
                    recyclerView.adapter = adapter
                }while(cursor.moveToNext())
            }
        }
        adapter.setOnItemClickListener { item, _->
            val info = item as MessageInfo
            var phoneNumber = info.phoneNumber
            val intent = Intent(context, ChatInterface::class.java)
            intent.putExtra("PHONE_NUMBER_FROMDATABASE",phoneNumber)
            context.startActivity(intent)
        }
        //return phoneNumber
    }

    fun deleteMessage(phoneNumber: String){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, phoneNumber)
        val success = db.delete(TABLE_RECENT_MESSAGE, "id=$phoneNumber",null)
        db.close()
    }

    fun updateMessage(phoneNumber: String, message: String, time: Long){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, phoneNumber)
        contentValues.put(KEY_MESSAGE, message)
        contentValues.put(KEY_TIME, time)
        val success = db.update(TABLE_RECENT_MESSAGE, contentValues, "id=$phoneNumber", null)
        db.close()
    }

    fun write(context: Context, dbName: String, text: String, source: String, time: String){
        var db = "$dbName.csv"
        var newline = System.getProperty("line.separator")?.toByteArray()
        try{
            var fos = context.openFileOutput(db, Context.MODE_APPEND + Context.MODE_PRIVATE)
            println("FileOutputStream: $db")
            fos.write(text.toByteArray())
            fos.write("*,*".toByteArray());
            fos.write(source.toByteArray())
            fos.write(("*,*".toByteArray()))
            fos.write(time.toByteArray())
            fos.write(newline)
            fos.close()
        }catch(e: IOException){
            Log.i("TAG", e.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun currentTime(): String{
        val time = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm")
        val formatted = time.format(formatter)
        return formatted
    }
}


class MessageInfo(private val message: String, val phoneNumber: String, val date: String): Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.show_recent_text
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txtMessage.text = message
        viewHolder.itemView.txtPhoneNumber.text = phoneNumber
        viewHolder.itemView.txtTest.text = date
    }


}
