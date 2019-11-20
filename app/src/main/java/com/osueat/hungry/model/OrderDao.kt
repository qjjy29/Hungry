package com.osueat.hungry.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

class OrderDao(private val databaseRef: DatabaseReference) {

    private val TAG = "OrderDao"

    fun constructOrderByHashMap(dataSnapshot: DataSnapshot): Order {
        val id = dataSnapshot.child("id").value as String
        val customerId = dataSnapshot.child("customerId").value as String
        val vendorId = dataSnapshot.child("vendorId").value as String
        val truckId = dataSnapshot.child("truckId").value as String
        val orderedFoodList = dataSnapshot.child("orderedFoodList").value as List<OrderedFood>
        val status = dataSnapshot.child("status").value as String
        val price = dataSnapshot.child("price").getValue(Double::class.java)
        val paymentId = dataSnapshot.child("paymentId").getValue(String::class.java)
        val createDate = dataSnapshot.child("createDate").getValue(Date::class.java)
        val lastUpdateDate = dataSnapshot.child("lastUpdateDate").getValue(Date::class.java)
        return Order(id, customerId, vendorId, truckId, orderedFoodList,
            status, price!!, paymentId, createDate!!, lastUpdateDate!!)
    }

    fun createOrder(order: Order) {
        databaseRef.child("orders").child(order.id).setValue(order)
        Log.d(TAG, order.id)
    }

    fun updateOrderById(id: String, order: Order) {
        databaseRef.child("orders").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                Log.d(TAG, result.toString())
                if (result != null) {
                    databaseRef.child("orders").child(id).setValue(order)
                    Log.d(TAG, "order updated")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    fun deleteOrderById(id: String) {
        databaseRef.child("orders").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                if (result != null) {
                    databaseRef.child("orders").child(id).removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }
}