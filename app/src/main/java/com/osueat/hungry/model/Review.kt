package com.osueat.hungry.model

data class Review (
    val id: String,
    val customerId: String,
    val truckId: String,
    val customerNickname: String,
    val content: String,
    val star: Int,
    val createDate: Long
)