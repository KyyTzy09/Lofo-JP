package com.fiky.lofo_app.data.models

import com.google.gson.annotations.SerializedName


data class ProfileModel(
    @SerializedName("user_id")
    val userId  : String,
    val username: String,
    val info    : String?,
    val address:  String?,
    val user : UserModel?
)
