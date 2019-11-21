package com.osueat.hungry.model

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import org.junit.Test

import org.junit.Assert.*

class TruckDaoTest {

    /*
        Unit Test to ensure that constructTruckByHashMap properly constructs a truck type
        and that truck stored in database matches the truck added to the database when retrieved
     */
    @Test
    fun constructTruckByHashMap() {
        val id = "test_truck_id"
        val name = "TestTruck"
        val address = "TestAddress"
        val foodIdList = ArrayList<String>()
        foodIdList.add("111")
        foodIdList.add("222")
        foodIdList.add("333")
        val vendorId = "test_vendor_id"
        val isActive = false
        val latitude = 81.234
        val longitude = 26.115

        // add test truck entry to database
        val ref = FirebaseDatabase.getInstance().reference
        val truckDao = TruckDao(ref)
        val testTruck = Truck(id, name, address, foodIdList, vendorId, isActive, latitude, longitude)
        truckDao.createTruck(testTruck)

        // create fake truck
        val id2 = "test_truck_id_2"
        // alter id so that newTruck only equals testTruck if value is retrieved properly from database
        var newTruck = Truck(id2, name, address, foodIdList, vendorId, isActive, latitude, longitude)

        ref.child("trucks").child(testTruck.id).
            addChildEventListener(object: ChildEventListener {
                override fun onChildRemoved(p0: DataSnapshot) {
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                    newTruck = truckDao.constructTruckByHashMap(dataSnapshot)
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })

        // assert that truck received from database equals truck added to database
        assertEquals(testTruck, newTruck)
    }
}