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

    private fun constructOrderByHashMap(orderMap: HashMap<String, Objects>): Order {
        val id = orderMap["id"] as String
        val customerId = orderMap["customerId"] as String
        val vendorId = orderMap["vendorId"] as String
        val truckId = orderMap["truckId"] as String
        val orderedFoodList = orderMap["orderedFoodList"] as List<OrderedFood>
        val status = orderMap["status"] as String
        val price = orderMap["price"] as Double
        val createDate = orderMap["createDate"] as Long
        val lastUpdateDate = orderMap["lastUpdateDate"] as Long
        return Order(id, customerId, vendorId, truckId, orderedFoodList,
            status, price, createDate, lastUpdateDate)
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