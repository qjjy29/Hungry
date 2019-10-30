package com.osueat.hungry.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

class VendorDao(private val databaseRef: DatabaseReference) {

    private fun constructVendorByHashMap(dataSnapshot: DataSnapshot): Vendor {
        val id = dataSnapshot.child("id") as String
        val userId = dataSnapshot.child("userId") as String
        val nickname = dataSnapshot.child("nickname") as String
        val truckIdList = dataSnapshot.child("truckIdList") as List<String>
        val balance = dataSnapshot.child("balance").getValue(Double::class.java)
        return Vendor(id, userId, nickname, truckIdList, balance!!)
    }

    fun createVendor(vendor: Vendor) {
        databaseRef.child("vendors").child(vendor.id).setValue(vendor)
    }

    fun updateVendorById(id: String, vendor: Vendor) {
        databaseRef.child("vendors").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                if (result != null) {
                    databaseRef.child("vendors").child(id).setValue(vendor)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    fun deleteVendorById(id: String) {
        databaseRef.child("vendors").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                if (result != null) {
                    databaseRef.child("vendors").child(id).removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }
}