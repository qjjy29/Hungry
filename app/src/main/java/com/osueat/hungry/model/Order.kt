package com.osueat.hungry.model

import java.util.*

data class Order (
    val id: String,
    val customerId: String,
    val vendorId: String,
    val truckId: String,
    val orderedFoodList: List<OrderedFood>,
    val status: String,
    val price: Double,
    val createDate: Date,
    val lastUpdateDate: Date
)