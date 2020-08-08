package com.hotapp229.messenger

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import java.sql.Date
import java.sql.Timestamp

class MainActivity : AppCompatActivity() {

    private val requestReceiveSms: Int = 1
    private val requestSendSms: Int = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions()

        receiveSms()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate: MenuInflater = menuInflater
        inflate.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessage::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }


    private fun checkPermissions() {
        when {
            ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), requestSendSms)
            }
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), requestSendSms)
            }
            ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS), requestReceiveSms)
            }
        }

    }
    private fun receiveSms(){
        var broadcastReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    for(sms in Telephony.Sms.Intents.getMessagesFromIntent(intent)){
                        val display = sms.displayMessageBody
                        val phone = sms.displayOriginatingAddress
                        val time = Timestamp(sms.timestampMillis)
                        val date = Date(time.time)
                        //message_receive.setText(sms.displayMessageBody)
                        println("Display Message: $display --- $phone ---$date")
                    }
                }
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }
}
