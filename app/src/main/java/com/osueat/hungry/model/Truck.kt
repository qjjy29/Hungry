package com.osueat.hungry.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Truck (
    val id : String,
    val name : String,
    val address : String,
    val foodIdList: ArrayList<String>,
    val vendorId : String,
    val isActive : Boolean,
    val latitude : Double,
    val longitude: Double
) : Parcelable