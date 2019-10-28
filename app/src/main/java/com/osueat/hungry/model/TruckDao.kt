package com.osueat.hungry.model

import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.R
import java.util.*

class TruckDao {

    val truckList = ArrayList<Truck>()
    val ref = FirebaseDatabase.getInstance().reference.child("trucks")

    fun constructTruckByHashMap(truckMap: HashMap<String, Objects>): Truck {
        val id = truckMap["id"] as String
        val name = truckMap["name"] as String
        val address = truckMap["address"] as String
        val foodIdList = truckMap["foodIdList"] as ArrayList<String>
        val vendorId = truckMap["vendorId"] as String
        return Truck(id, name, address, foodIdList, vendorId)
    }

    fun createTruck(truck: Truck) {
        val id = truck.id
        ref.child(id).setValue(truck)
        truckList.add(truck)
    }

    fun updateTruckById(truckId: String, newTruck: Truck) {
        ref.child(truckId).setValue(newTruck)
    }

    fun deleteTruckById(truckId: String) {
        ref.child(truckId).removeValue()
    }
}