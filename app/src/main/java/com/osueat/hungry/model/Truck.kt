package com.osueat.hungry.model

import java.util.*

class Truck (truckName: String, truckAddress: String) {
    //val id = UUID.randomUUID()
    private val name = truckName
    private val address = truckAddress

    // THIS WILL NOT WORK WITH FIREBASE WITHOUT THE GETTERS
    fun getName(): String {
        return name
    }

    fun getAddress(): String{
        return address
    }
}