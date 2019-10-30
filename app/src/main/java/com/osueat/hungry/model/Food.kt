package com.osueat.hungry.model

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Food (
    val id: String,
    val truckId: String,
    val name: String,
    val price: Double,
    val description: String,
    val createDate: Date,
    val lastUpdateDate: Date
)