package com.hotapp229.messenger

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.new_message.*
import kotlinx.android.synthetic.main.new_message.view.*
import kotlinx.android.synthetic.main.receiver.view.*
import kotlinx.android.synthetic.main.sender.view.*
import kotlinx.android.synthetic.main.show_recent_text.view.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NewMessage : AppCompatActivity() {
    private val adapter = GroupAdapter<GroupieViewHolder>()
    //private var phoneNumber: String? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_message)
        supportActionBar?.title = "New Message"
        val name: String? = intent.getStringExtra("CONTACT_NAME")
        //phoneNumber = intent.getStringExtra("PHONE_NUMBER")
        btnSendMessage.setOnClickListener {
            val DBMessage = DBShowRecentMessage(this)
            if(edtMessage.length() > 0) {
                val enigma = SuperMagic()
                val encryptedMessage = enigma.startEncrypting(edtMessage.text.toString(),this)
                adapter.add(Message(encryptedMessage))
                new_message_recyclerview.adapter = adapter
                btnOpenContactList.alpha = 0f

                DBMessage.saveMessage(edtContact.text.toString(), encryptedMessage,DBMessage.currentTime(),"unread")
                DBMessage.createDatabase(edtContact.text.toString(), encryptedMessage, DBMessage.currentTime(), "*sender*")
                DBMessage.write(this,edtContact.text.toString(), encryptedMessage, "*sender*", DBMessage.currentTime())
                edtMessage.setText("")
            }
            else
            {
                Toast.makeText(this,"Please insert some text",Toast.LENGTH_SHORT).show()
            }
        }
        edtContact.setText(name)
        populateContactList()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, Setting::class.java)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }

    private fun populateContactList(){
        btnOpenContactList.setOnClickListener {
            Toast.makeText(this, "Populate Contact List", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ContactList::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate: MenuInflater = menuInflater
        inflate.inflate(R.menu.new_message_settings,menu)
        return true
    }

    class Message(private val mess: String): Item<GroupieViewHolder>(){
        override fun getLayout(): Int {
            return R.layout.sender
        }

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.txtSender.text = mess
        }

    }

    class Receiver(private val mess: String): Item<GroupieViewHolder>(){
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.txtReceiver.text = mess
        }

        override fun getLayout(): Int {
            return R.layout.receiver
        }

    }
}
