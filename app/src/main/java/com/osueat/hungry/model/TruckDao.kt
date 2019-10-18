package com.osueat.hungry.model

import java.util.*

class TruckDao {
    fun constructTruckByHashMap(truckMap: HashMap<String, Objects>): Truck {
        val name = truckMap["name"] as String
        val address = truckMap["address"] as String
        return Truck(name, address)
    }
}