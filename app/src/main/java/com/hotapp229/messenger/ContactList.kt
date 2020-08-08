package com.hotapp229.messenger

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_contact_list.*
import kotlinx.android.synthetic.main.add_contact_list.*
import kotlinx.android.synthetic.main.add_contact_list.view.*

class ContactList : AppCompatActivity() {
    private val myAdapter = GroupAdapter<GroupieViewHolder>()
    val phoneNumber: Int = 0
    val ContactName: String = ""

    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
        const val CONTACT_NAME = "CONTACT_NAME"
        const val CONTACT_PHONE = "CONTACT_PHONE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        supportActionBar?.title = "Contact List"

        //loadContacts()
        myAdapter.setOnItemClickListener { item, _ ->
            val contact = item as Contact
            val intent = Intent(this,NewMessage::class.java)
            intent.putExtra(CONTACT_NAME,contact.name)
            intent.putExtra(CONTACT_PHONE,contact.phoneNumber)
            startActivity(intent)
            //Toast.makeText(this,"${contact.name} --- ${contact.phoneNumber}", Toast.LENGTH_SHORT).show()
        }
        getAllContacts()
    }


    private fun getAllContacts() {
        val contResolver = contentResolver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS)
            //callback onRequestPermissionsResult
        }
        else {
            val cursor: Cursor? =
                contResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
            var listContact = ArrayList<Contact>()
            if (cursor?.count!! > 0) {
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        val id: String =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)) // get the unique ID for a row
                        if (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                                .toInt() > 0
                        ) {
                            val element: Cursor? = contResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                arrayOf(id),
                                null
                            )
                            if (element != null) {
                                while (element.moveToNext()) {
                                    val phone: String =
                                        element.getString(element.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                    val name: String =
                                        element.getString(element.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                                    listContact.add(Contact(name, phone))
                                }
                            }
                            element?.close()
                        }
                    }
                }
                for (i in listContact.sortedBy { it.name }) {
                    val name = i.name
//                    if (name.startsWith("C")) {
//                        println(i.name)
//                        myAdapter.add(i)
//                        contact_list_view.adapter = myAdapter
//                    }
                     myAdapter.add(i)
                }
                contact_list_view.adapter = myAdapter
            }
        }
    }

    class Contact(val name: String, val phoneNumber: String): Item<GroupieViewHolder>(){
        override fun getLayout(): Int {
            return R.layout.add_contact_list
        }

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.txvContact.text = name

        }

    }
}
