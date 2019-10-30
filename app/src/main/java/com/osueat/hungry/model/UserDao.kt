package com.osueat.hungry.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

class UserDao(private val databaseRef: DatabaseReference) {

    private val TAG = "UserDao"

    fun constructUserByHashMap(dataSnapshot: DataSnapshot): User {
        val id = dataSnapshot.child("id").value as String
        val username = dataSnapshot.child("username").value as String
        val password = dataSnapshot.child("password").value as String
        val createDate = dataSnapshot.child("createDate").getValue(Date::class.java)
        val lastLoginDate = dataSnapshot.child("lastLoginDate").getValue(Date::class.java)
        val lastUpdateDate = dataSnapshot.child("lastUpdateDate").getValue(Date::class.java)
        val phoneNumber = dataSnapshot.child("phoneNumber").value as String
        val email = dataSnapshot.child("email").value as String
        val type = dataSnapshot.child("type").value as String
        return User(id, username, password, createDate!!, lastUpdateDate!!,
            lastLoginDate!!, phoneNumber, email, type)
    }

    fun createUser(user: User) {
        databaseRef.child("users").child(user.id).setValue(user)
        Log.d(TAG, user.username)
    }

    fun updateUserById(id: String, user: User) {
        databaseRef.child("users").child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                Log.d(TAG, result.toString())
                if (result != null) {
                    databaseRef.child("users").child(id).setValue(user)
                    Log.d(TAG, "user updated")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    fun deleteUserById(id: String) {
        databaseRef.child("users").child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                if (result != null) {
                    databaseRef.child("users").child(id).removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }
}
