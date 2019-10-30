package com.osueat.hungry.model

import java.util.*

data class Payment(
    val id: String,
    val customerId: String,
    val vendorId: String,
    val orderId: String,
    val amount: Double,
    val createDate: Date,
    val lastUpdateDate: Date
)