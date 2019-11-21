package com.osueat.hungry.model

import org.junit.Test

import org.junit.Assert.*
import java.util.ArrayList

class TruckUtilityTest {

    @Test
    /*
        Tests whether function properly retrieves only active trucks.
     */
    fun getActiveTrucksFromList() {
        // create some test trucks
        val id = "Test ID 1"
        val id2 = "Test ID 2"
        val name = "Test Truck 1"
        val address = "Test Address 1"
        val foodIdList = ArrayList<String>()
        foodIdList.add("Food ID 1")
        foodIdList.add("Food ID 2")
        val vendorId = "Vendor ID 1"
        val latitude = 23.155
        val longitude = 213.22

        var isActive = true

        val activeTruck1 = Truck(id, name, address, foodIdList, vendorId, isActive, latitude, longitude)
        val activeTruck2 = Truck(id2, name, address, foodIdList, vendorId, isActive, latitude, longitude)

        isActive = false
        val inactiveTruck = Truck(id, name, address, foodIdList, vendorId, isActive, latitude, longitude)

        val truckList = ArrayList<Truck>()
        truckList.add(activeTruck1)
        truckList.add(activeTruck2)
        truckList.add(inactiveTruck)

        // assert that only the active trucks are returned to user
        val truckUtility = TruckUtility()
        val activeTruckList = truckUtility.GetActiveTrucksFromList(truckList)

        val expectedTruckList = ArrayList<Truck>()
        expectedTruckList.add(activeTruck1)
        expectedTruckList.add(activeTruck2)

        assertEquals(expectedTruckList, activeTruckList)
    }

    @Test
    /*
        Tests whether function properly retrieves only inactive trucks.
     */
    fun getInactiveTrucksFromList() {
        // create some test trucks
        val id = "Test ID 1"
        val id2 = "Test ID 2"
        val name = "Test Truck 1"
        val address = "Test Address 1"
        val foodIdList = ArrayList<String>()
        foodIdList.add("Food ID 1")
        foodIdList.add("Food ID 2")
        val vendorId = "Vendor ID 1"
        val latitude = 23.155
        val longitude = 213.22

        var isActive = true

        val activeTruck1 = Truck(id, name, address, foodIdList, vendorId, isActive, latitude, longitude)
        val activeTruck2 = Truck(id2, name, address, foodIdList, vendorId, isActive, latitude, longitude)

        isActive = false
        val inactiveTruck = Truck(id, name, address, foodIdList, vendorId, isActive, latitude, longitude)

        val truckList = ArrayList<Truck>()
        truckList.add(activeTruck1)
        truckList.add(activeTruck2)
        truckList.add(inactiveTruck)

        // assert that only the inactive trucks are returned to user
        val truckUtility = TruckUtility()
        val inactiveTruckList = truckUtility.GetInactiveTrucksFromList(truckList)

        val expectedTruckList = ArrayList<Truck>()
        expectedTruckList.add(inactiveTruck)

        assertEquals(expectedTruckList, inactiveTruckList)
    }

    @Test
    /*
       Tests whether function properly deactivates all trucks.
    */
    fun DeactivateAllActiveTrucksInList() {
        // create some test trucks
        val id = "Test ID 1"
        val id2 = "Test ID 2"
        val id3 = "Test ID 2"
        val name = "Test Truck 1"
        val address = "Test Address 1"
        val foodIdList = ArrayList<String>()
        foodIdList.add("Food ID 1")
        foodIdList.add("Food ID 2")
        val vendorId = "Vendor ID 1"
        val latitude = 23.155
        val longitude = 213.22

        var isActive = true

        val activeTruck1 = Truck(id, name, address, foodIdList, vendorId, isActive, latitude, longitude)
        val activeTruck2 = Truck(id2, name, address, foodIdList, vendorId, isActive, latitude, longitude)

        isActive = false
        val deactivatedTruck1 = Truck(id, name, address, foodIdList, vendorId, isActive, latitude, longitude)
        val deactivatedTruck2 = Truck(id2, name, address, foodIdList, vendorId, isActive, latitude, longitude)
        val inactiveTruck = Truck(id3, name, address, foodIdList, vendorId, isActive, latitude, longitude)

        val truckList = ArrayList<Truck>()
        truckList.add(activeTruck1)
        truckList.add(activeTruck2)
        truckList.add(inactiveTruck)

        // assert that only the inactive trucks are returned to user
        val truckUtility = TruckUtility()
        val deactivatedTruckList = truckUtility.DeactivateAllActiveTrucksInList(truckList)

        val expectedTruckList = ArrayList<Truck>()
        expectedTruckList.add(deactivatedTruck1)
        expectedTruckList.add(deactivatedTruck2)
        expectedTruckList.add(inactiveTruck)

        assertEquals(expectedTruckList, deactivatedTruckList)
    }
}
