package com.osueat.hungry.model

import java.util.*

data class Review (
    val id: String,
    val customerId: String,
    val truckId: String,
    val customerNickname: String,
    val content: String,
    val star: Int,
    val createDate: Date
)