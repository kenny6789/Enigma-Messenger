package com.hotapp229.messenger

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    /*
    Nên làm cái check nếu như cái enigma chưa setup thì báo lỗi
     */
    private val requestReceiveSms: Int = 1
    private val requestSendSms: Int = 2
    private val dbShowRecentMessage = DBShowRecentMessage(this)

    var phoneNumber: String? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions()
        dbShowRecentMessage.viewMessage(main_acitivity_contact_list, this)
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
                intent.putExtra("PHONE_NUMBER",phoneNumber)
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
    //function này đã làm enigma
    private fun receiveSms() {
        val DBMessage = DBShowRecentMessage(this)
        var broadcastReceiver = object : BroadcastReceiver(){
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onReceive(context: Context?, intent: Intent) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    for(sms in Telephony.Sms.Intents.getMessagesFromIntent(intent)){
                        val enigma = SuperMagic()
                        if(context != null) {
                            val message = enigma.startEncrypting(sms.displayMessageBody, context)
                            val phone = sms.displayOriginatingAddress
                            DBMessage.saveMessage(phone, message, DBMessage.currentTime(), "unread")
                            DBMessage.createDatabase(
                                "SampleTable",
                                message,
                                DBMessage.currentTime(),
                                "incoming"
                            )
                            DBMessage.write(
                                context,
                                phone,
                                message,
                                "incoming",
                                DBMessage.currentTime()
                            )
                        }
                        //phoneNumber = phone
                    }
                }
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }
}

