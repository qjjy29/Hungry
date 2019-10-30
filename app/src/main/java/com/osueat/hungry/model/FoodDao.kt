package com.osueat.hungry.model

import com.google.firebase.database.FirebaseDatabase
import java.util.*

class FoodDao {

    val foodList = ArrayList<Food>()
    val ref = FirebaseDatabase.getInstance().reference.child("food")

    fun constructFoodByHashMap(foodMap: HashMap<String, Objects>): Food {
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
        val id = food.id
        ref.child(id).setValue(food)
        foodList.add(food)
    }
}