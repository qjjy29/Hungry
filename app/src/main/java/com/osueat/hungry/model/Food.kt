package com.osueat.hungry.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize
import java.util.*

@IgnoreExtraProperties
@Parcelize
data class Food (
    val id: String,
    val truckId: String,
    val name: String,
    val price: Double,
    val description: String,
    val createDate: Date,
    val lastUpdateDate: Date
) : Parcelable