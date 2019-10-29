package com.osueat.hungry.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Food (
    val id: String,
    val truckId: String,
    val name: String,
    val price: Double,
    val description: String,
    val createDate: Long,
    val lastUpdateDate: Long
)