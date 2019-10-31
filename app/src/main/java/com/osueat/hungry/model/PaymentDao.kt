package com.osueat.hungry.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

class PaymentDao(private val databaseRef: DatabaseReference) {

    private val TAG = "PaymentDao"

    fun constructPaymentByHashMap(dataSnapshot: DataSnapshot): Payment {
        val id = dataSnapshot.child("id").value as String
        val customerId = dataSnapshot.child("customerId").value as String
        val vendorId = dataSnapshot.child("vendorId").value as String
        val orderId = dataSnapshot.child("orderId").value as String
        val amount = dataSnapshot.child("amount").getValue(Double::class.java)
        val createDate = dataSnapshot.child("createDate").getValue(Date::class.java)
        val lastUpdateDate = dataSnapshot.child("lastUpdateDate").getValue(Date::class.java)
        return Payment(id, customerId, vendorId, orderId, amount!!, createDate!!, lastUpdateDate!!)
    }

    fun createPayment(payment: Payment) {
        databaseRef.child("payments").child(payment.id).setValue(payment)
        Log.d(TAG, payment.id)
    }

    fun updatePaymentById(id: String, payment: Payment) {
        databaseRef.child("payments").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                Log.d(TAG, result.toString())
                if (result != null) {
                    databaseRef.child("payments").child(id).setValue(payment)
                    Log.d(TAG, "payment updated")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    fun deletePaymentById(id: String) {
        databaseRef.child("payments").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                if (result != null) {
                    databaseRef.child("payments").child(id).removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }
}