package com.osueat.hungry.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Review (
    val id: String,
    val customerId: String,
    val truckId: String,
    val customerNickname: String,
    val content: String,
    val star: Int,
    val createDate: Date
) : Parcelable