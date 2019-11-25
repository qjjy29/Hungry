package com.osueat.hungry.ui.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.R
import com.osueat.hungry.data.model.CurrentCustomer
import com.osueat.hungry.model.Customer
import com.osueat.hungry.model.CustomerDao

class CustomerProfileActivity : AppCompatActivity() {

    private val ref = FirebaseDatabase.getInstance().reference

    private fun getCustomerInfo() {
        val currentCustomer = CurrentCustomer.getCustomer()
        if (currentCustomer != null) {
            ref.child("customers").child(currentCustomer.id)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val customerDao = CustomerDao(ref)
                        val customer = customerDao.constructCustomerByHashMap(dataSnapshot)
                        val nameText = findViewById<TextView>(R.id.customerNameText)
                        val balanceText = findViewById<TextView>(R.id.customerBalanceText)
                        nameText.text = customer.nickname
                        balanceText.text = customer.balance.toString()
                        CurrentCustomer.setCustomer(customer)

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        TODO("not implemented")
                    }
                })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_profile)
    }

    override fun onStart() {
        super.onStart()
        getCustomerInfo()
    }
}
