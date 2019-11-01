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

    fun constructTruckByHashMap(dataSnapshot: DataSnapshot): Truck {
        val id = dataSnapshot.child("id").value as String
        val name = dataSnapshot.child("name").value as String
        val address = dataSnapshot.child("address").value as String
        val foodIdList = dataSnapshot.child("foodIdList").value as ArrayList<String>
        val vendorId = dataSnapshot.child("vendorId").value  as String
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