package com.osueat.hungry.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

class SubscribeDao(private val databaseRef: DatabaseReference) {

    private val TAG = "SubscribeDao"

    fun constructSubscribeByHashMap(dataSnapshot: DataSnapshot): Subscribe {
        val id = dataSnapshot.child("id").value as String
        val customerId = dataSnapshot.child("customerId").value as String
        val truckId = dataSnapshot.child("truckId").value as String
        val createDate = dataSnapshot.child("createDate").getValue(Date::class.java)
        return Subscribe(id, customerId, truckId, createDate!!)
    }

    fun createSubscribe(subscribe: Subscribe) {
        databaseRef.child("subscribes").child(subscribe.id).setValue(subscribe)
        Log.d(TAG, subscribe.id)
    }

    fun updateSubscribeById(id: String, subscribe: Subscribe) {
        databaseRef.child("subscribes").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                Log.d(TAG, result.toString())
                if (result != null) {
                    databaseRef.child("subscribes").child(id).setValue(subscribe)
                    Log.d(TAG, "subscribe updated")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    fun deleteSubscribeById(id: String) {
        databaseRef.child("subscribes").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                if (result != null) {
                    databaseRef.child("subscribes").child(id).removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }
}