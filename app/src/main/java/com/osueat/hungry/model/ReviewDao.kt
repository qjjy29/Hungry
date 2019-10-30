package com.osueat.hungry.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

class ReviewDao(private val databaseRef: DatabaseReference) {

    private val TAG = "ReviewDao"

    fun constructReviewByHashMap(dataSnapshot: DataSnapshot): Review {
        val id = dataSnapshot.child("id") as String
        val customerId = dataSnapshot.child("customerId") as String
        val truckId = dataSnapshot.child("truckId") as String
        val customerNickname = dataSnapshot.child("customerNickname") as String
        val content = dataSnapshot.child("content") as String
        val star = dataSnapshot.child("star") as Int
        val createDate = dataSnapshot.child("createDate").getValue(Date::class.java)
        return Review(id, customerId, truckId, customerNickname, content, star, createDate!!)
    }

    fun createReview(review: Review) {
        databaseRef.child("reviews").child(review.id).setValue(review)
        Log.d(TAG, review.id)
    }

    fun updateReviewById(id: String, review: Review) {
        databaseRef.child("reviews").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                Log.d(TAG, result.toString())
                if (result != null) {
                    databaseRef.child("reviews").child(id).setValue(review)
                    Log.d(TAG, "review updated")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    fun deleteReviewById(id: String) {
        databaseRef.child("reviews").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                if (result != null) {
                    databaseRef.child("reviews").child(id).removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }
}