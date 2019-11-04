package com.osueat.hungry.model

import android.widget.ListView
import com.google.firebase.database.*
import com.osueat.hungry.R
import java.util.*

class TruckDao(private val databaseRef: DatabaseReference) {

    val truckList = ArrayList<Truck>()

    fun constructTruckByHashMap(dataSnapshot: DataSnapshot): Truck {
        val id = dataSnapshot.child("id").value as String
        val name = dataSnapshot.child("name").value as String
        val address = dataSnapshot.child("address").value as String
        val foodIdList = dataSnapshot.child("foodIdList").value as ArrayList<String>
        val vendorId = dataSnapshot.child("vendorId").value  as String
        val isActive = dataSnapshot.child("active").getValue(Boolean::class.java)
        return Truck(id, name, address, foodIdList, vendorId, isActive!!)
    }

    fun createTruck(truck: Truck) {
        val id = truck.id
        databaseRef.child("trucks").child(id).setValue(truck)
        truckList.add(truck)
    }

    fun updateTruckById(truckId: String, newTruck: Truck) {
        databaseRef.child("trucks").child(truckId).setValue(newTruck)
    }

    fun deleteTruckById(truckId: String) {
        databaseRef.child("trucks").child(truckId).removeValue()
    }
}