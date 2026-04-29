package com.fiky.lofo_app.data.api.dto.auth

import com.fiky.lofo_app.data.models.UserModel
import com.google.gson.annotations.SerializedName
data class RegisterRequest(
    val username: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    val password: String,
    val address: String
)

data class RegisterResponse (
    val message: String,
    val data: UserModel
)