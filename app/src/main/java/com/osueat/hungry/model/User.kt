package com.osueat.hungry.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val id: String,
    val username: String,
    val password: String,
    val createDate: Long,
    val lastLoginDate: Long,
    val lastUpdateDate: Long,
    val phoneNumber: String,
    val email: String,
    val type: String
)