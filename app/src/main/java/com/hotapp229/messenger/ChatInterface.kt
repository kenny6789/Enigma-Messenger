package com.hotapp229.messenger

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_interface.*
import kotlinx.android.synthetic.main.new_message.*
import kotlinx.android.synthetic.main.receiver.view.*
import kotlinx.android.synthetic.main.sender.view.*
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatInterface: AppCompatActivity() {
    private val adapter = GroupAdapter<GroupieViewHolder>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_interface)
        read()
        btnSend.setOnClickListener {
            val dbShowRecentMessage = DBShowRecentMessage(this)
            if(edtText.length() > 0){
                val getPhoneNumber = intent.getStringExtra("PHONE_NUMBER_FROMDATABASE")
                val enigma = SuperMagic()
                val encryptedMessage = enigma.startEncrypting(edtText.text.toString(),this)
                adapter.add(Sender(encryptedMessage, this))
                chat_recyclverview.adapter = adapter
                dbShowRecentMessage.write(this,getPhoneNumber,encryptedMessage,"*sender*", dbShowRecentMessage.currentTime())
                dbShowRecentMessage.saveMessage(getPhoneNumber, encryptedMessage,dbShowRecentMessage.currentTime(),"unread")
                edtText.setText("")
            }
        }

    }
    private fun read(){
        val getPhoneNumber = intent.getStringExtra("PHONE_NUMBER_FROMDATABASE")
        txtPhoneNumber.text = getPhoneNumber
        var fileInputStream: FileInputStream? = null
        var fileName = "$getPhoneNumber.csv"
        try {
            fileInputStream = openFileInput(fileName)
            var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                var temp = text!!.split("*,*")
                val source = temp[1]
                if(source == "incoming"){
                    adapter.add(Receiver(temp[0],this))
                    chat_recyclverview.adapter = adapter
                }
                else {
                    adapter.add(Sender(temp[0], this))
                    chat_recyclverview.adapter = adapter
                }
            }
        }catch (e: FileNotFoundException){
            println(e.toString())
        }
    }
    class Sender(private val mess: String, context: Context): Item<GroupieViewHolder>(){
        private val enigma = SuperMagic()
        private var message = enigma.startEncrypting(mess, context)
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.txtSender.text = mess
            viewHolder.itemView.setOnClickListener {
                viewHolder.itemView.txtSender.text = message
            }
            viewHolder.itemView.setOnDragListener { v, event ->
                when(event.action){

                }
            }
        }
        override fun getLayout(): Int {
            return R.layout.sender
        }
    }
    class Receiver(private val mess: String, context: Context): Item<GroupieViewHolder>(){
        private val enigma = SuperMagic()
        private var message = enigma.startEncrypting(mess, context)
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.txtReceiver.text = mess
            viewHolder.itemView.setOnClickListener {
                viewHolder.itemView.txtReceiver.text = message
            }

        }
        override fun getLayout(): Int {
            return R.layout.receiver
        }
    }
}