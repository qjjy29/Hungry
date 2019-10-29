package com.osueat.hungry.model

data class Order (
    val id: String,
    val customerId: String,
    val vendorId: String,
    val truckId: String,
    val orderedFoodList: List<OrderedFood>,
    val status: String,
    val price: Double,
    val createDate: Long,
    val lastUpdateDate: Long
)