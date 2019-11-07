package com.osueat.hungry.data.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.model.CustomerDao
import com.osueat.hungry.model.User
import com.osueat.hungry.model.UserDao
import com.osueat.hungry.model.VendorDao

object CurrentUser {
    private var currentUser: User? = null
    private val TAG = "CurrentUser"
    private val ref = FirebaseDatabase.getInstance().reference

    fun getCurrentUser(): User? {
        return currentUser
    }

    fun setCurrentUser(user: User) {
        val customerDao = CustomerDao(ref)
        val vendorDao = VendorDao(ref)
        currentUser = user
        if (user.type == "CUSTOMER") {
            ref.child("customers").orderByChild("userId").equalTo(user.id)
                .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (dataSnapshotChild in dataSnapshot.children) {
                        val customer = customerDao.constructCustomerByHashMap(dataSnapshotChild)
                        CurrentCustomer.setCustomer(customer)
                        break
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("The read failed: " + databaseError.code)
                }
            })
        } else if (user.type == "VENDOR") {
            ref.child("vendors").orderByChild("userId").equalTo(user.id)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (dataSnapshotChild in dataSnapshot.children) {
                            val vendor = vendorDao.constructVendorByHashMap(dataSnapshotChild)
                            CurrentVendor.setCurrentVendor(vendor)
                            break
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        println("The read failed: " + databaseError.code)
                    }
                })
        }
    }
}