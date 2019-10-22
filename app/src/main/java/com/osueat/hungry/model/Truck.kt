package com.osueat.hungry.model

import java.util.*

class Truck (truckId: String, truckName: String, truckAddress: String) {
    //val id = UUID.randomUUID()
    private val id = truckId
    private val name = truckName
    private val address = truckAddress

    // THIS WILL NOT WORK WITH FIREBASE WITHOUT THE GETTERS
    fun getId(): String {
        return id
    }

    fun getName(): String {
        return name
    }

    fun getAddress(): String{
        return address
    }
}