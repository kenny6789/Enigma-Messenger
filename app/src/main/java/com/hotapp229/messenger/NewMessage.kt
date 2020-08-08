package com.hotapp229.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.new_message.*
import kotlinx.android.synthetic.main.new_message.view.*
import kotlinx.android.synthetic.main.sender.view.*

class NewMessage : AppCompatActivity() {
    private val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_message)
        supportActionBar?.title = "New Message"

        val name: String? = intent.getStringExtra("CONTACT_NAME")
        val phone: String? = intent.getStringExtra("CONTACT_PHONE")

        btnSendMessage.setOnClickListener {
            if(edtMessage.length() > 0) {
                adapter.add(Message(edtMessage.text.toString()))
                recyclerview_message.adapter = adapter
                btnOpenContactList.alpha = 0f
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

}
