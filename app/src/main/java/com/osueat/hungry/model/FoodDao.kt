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
    private val foodList = ArrayList<Food>()

    public fun constructFoodByHashMap(dataSnapshot: DataSnapshot): Food {
        val id = dataSnapshot.child("id").value as String
        val truckId = dataSnapshot.child("truckId").value as String
        val name = dataSnapshot.child("name").value as String
        val price = dataSnapshot.child("price").getValue(Double::class.java)
        val description = dataSnapshot.child("description").value as String
        val createDate = dataSnapshot.child("createDate").getValue(Date::class.java)
        val lastUpdateDate = dataSnapshot.child("lastUpdateDate").getValue(Date::class.java)
        return Food(id, truckId, name, price!!, description, createDate!!, lastUpdateDate!!)
    }

    fun createFood(food: Food) {
        databaseRef.child("foods").child(food.id).setValue(food)
        Log.d(TAG, food.name)
        foodList.add(food)
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

                    // update food in food list
                    for (i in 0..foodList.count()) {
                        if (foodList[i].id == id) {
                            foodList[i] = food
                        }
                    }
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

                    // delete food in food list
                    for (i in 0..foodList.count()) {
                        if (foodList[i].id == id) {
                            foodList.removeAt(i)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }
}