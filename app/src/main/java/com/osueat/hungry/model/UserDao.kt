package com.osueat.hungry.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

class UserDao(private val databaseRef: DatabaseReference) {

    private fun constructUserByHashMap(userMap: HashMap<String, Objects>): User {
        val id = userMap["id"] as String
        val username = userMap["username"] as String
        val password = userMap["password"] as String
        val createDate = userMap["createDate"] as Long
        val lastLoginDate = userMap["lastLoginDate"] as Long
        val lastUpdateDate = userMap["lastUpdateDate"] as Long
        val phoneNumber = userMap["phoneNumber"] as String
        val email = userMap["email"] as String
        val type = userMap["type"] as String
        return User(id, username, password, createDate, lastUpdateDate,
            lastLoginDate, phoneNumber, email, type)
    }

    fun createUser(user: User) {
        databaseRef.child("users").child(user.id).setValue(user)
    }

    fun updateUserById(id: String, user: User) {
        databaseRef.child("users").child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                if (result != null) {
                    databaseRef.child("users").child(id).setValue(user)
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
