package com.osueat.hungry.model

import java.util.*

class Truck (truckName: String, truckAddress: String) {
    //val id = UUID.randomUUID()
    private var name = truckName
    private var address = truckAddress


    fun setName(newName: String) {
        name = newName
    }

    fun getName(): String {
        return name
    }

    fun setAddress(newAddress: String) {
        address = newAddress
    }

    fun getAddress(): String{
        return address
    }
}