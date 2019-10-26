package com.osueat.hungry.model

import java.util.*

class FoodDao {
    fun constructFoodByHashMap(foodMap: HashMap<String, Objects>): Food {
        val id = foodMap["id"] as String
        val truckId = foodMap["truckId"] as String
        val name = foodMap["name"] as String
        val price = foodMap["price"] as Double
        val description = foodMap["description"] as String
        val createDate = foodMap["createDate"] as Int
        val lastUpdateDate = foodMap["lastUpdateDate"] as Int
        return Food(id, truckId, name, price, description, createDate, lastUpdateDate)
    }
}