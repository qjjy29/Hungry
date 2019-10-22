package com.osueat.hungry.model

import java.util.*

class TruckDao {
    fun constructTruckByHashMap(truckMap: HashMap<String, Objects>): Truck {
        val id = truckMap["id"] as String
        val name = truckMap["name"] as String
        val address = truckMap["address"] as String
        return Truck(id, name, address)
    }
}