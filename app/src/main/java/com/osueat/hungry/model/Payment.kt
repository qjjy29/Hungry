package com.osueat.hungry.model

data class Payment(
    val id: String,
    val customerId: String,
    val vendorId: String,
    val orderId: String,
    val amount: Double,
    val createDate: Long,
    val lastUpdateDate: Long
)