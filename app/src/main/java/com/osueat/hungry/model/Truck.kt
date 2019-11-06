package com.osueat.hungry.model

import java.util.*

data class Truck (
    val id : String,
    val name : String,
    val address : String,
    val foodIdList: ArrayList<String>,
    val vendorId : String,
    val isActive : Boolean,
    val latitude : Double,
    val longitude: Double
)