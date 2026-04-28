package com.fiky.lofo_app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileModel(
    @SerialName("user_id")
    val userId  : String,
    val username: String,
    val info    : String?,
    val address:  String?,
    val user : UserModel?
)
