package com.osueat.hungry.model

import java.util.*

data class Subscribe (
    val id: String,
    val customerId: String,
    val truckId: String,
    val createDate: Date
)