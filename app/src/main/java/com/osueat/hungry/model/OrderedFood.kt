package com.osueat.hungry.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderedFood (
    val foodId: String,
    val quantity: Int
) : Parcelable