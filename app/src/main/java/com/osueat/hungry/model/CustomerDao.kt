package com.osueat.hungry.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

class CustomerDao(private val databaseRef: DatabaseReference) {

    fun constructCustomerByHashMap(dataSnapshot: DataSnapshot): Customer {
        val id = dataSnapshot.child("id").value as String
        val userId = dataSnapshot.child("userId").value as String
        val nickname = dataSnapshot.child("nickname").value as String
        val balance = dataSnapshot.child("balance").getValue(Double::class.java)
        return Customer(id, userId, nickname, balance!!)
    }

    fun createCustomer(customer: Customer) {
        databaseRef.child("customers").child(customer.id).setValue(customer)
    }

    fun updateCustomerById(id: String, customer: Customer) {
        databaseRef.child("customers").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                if (result != null) {
                    databaseRef.child("customers").child(id).setValue(customer)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    fun deleteCustomerById(id: String) {
        databaseRef.child("customers").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                if (result != null) {
                    databaseRef.child("customers").child(id).removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }
}