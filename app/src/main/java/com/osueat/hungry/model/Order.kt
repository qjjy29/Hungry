package com.osueat.hungry.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

data class Order (
    val id: String,
    val customerId: String,
    val vendorId: String,
    val truckId: String,
    val orderedFoodList: List<OrderedFood>,
    val status: String,
    val price: Double,
    val paymentId: String?,
    val createDate: Date,
    val lastUpdateDate: Date
)