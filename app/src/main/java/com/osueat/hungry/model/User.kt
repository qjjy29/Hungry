package com.osueat.hungry.model

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class User(
    val id: String,
    val username: String,
    val password: String,
    val createDate: Date,
    val lastLoginDate: Date,
    val lastUpdateDate: Date,
    val phoneNumber: String,
    val email: String,
    val type: String
)