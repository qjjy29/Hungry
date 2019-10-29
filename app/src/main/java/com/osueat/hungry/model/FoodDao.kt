package com.osueat.hungry.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

class FoodDao(private val databaseRef: DatabaseReference) {

    private val TAG = "FoodDao"

    private fun constructFoodByHashMap(foodMap: HashMap<String, Objects>): Food {
        val id = foodMap["id"] as String
        val truckId = foodMap["truckId"] as String
        val name = foodMap["name"] as String
        val price = foodMap["price"] as Double
        val description = foodMap["description"] as String
        val createDate = foodMap["createDate"] as Long
        val lastUpdateDate = foodMap["lastUpdateDate"] as Long
        return Food(id, truckId, name, price, description, createDate, lastUpdateDate)
    }

    fun createFood(food: Food) {
        databaseRef.child("foods").child(food.id).setValue(food)
        Log.d(TAG, food.name)
    }

    fun updateFoodById(id: String, food: Food) {
        databaseRef.child("foods").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                Log.d(TAG, result.toString())
                if (result != null) {
                    databaseRef.child("foods").child(id).setValue(food)
                    Log.d(TAG, "food updated")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    fun deleteFoodById(id: String) {
        databaseRef.child("foods").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                if (result != null) {
                    databaseRef.child("foods").child(id).removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }
}