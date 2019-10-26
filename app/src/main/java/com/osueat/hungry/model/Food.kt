package com.osueat.hungry.model

data class Food (
    val id : String,
    val truckId : String,
    val name : String,
    val price : Double,
    val description : String,
    val createDate : Int,
    val lastUpdateDate : Int
)