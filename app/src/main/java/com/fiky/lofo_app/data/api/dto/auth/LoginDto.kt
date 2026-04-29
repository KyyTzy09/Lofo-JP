package com.fiky.lofo_app.data.api.dto.auth

import com.google.gson.annotations.SerializedName
data class LoginRequest(
    @SerializedName("phone_number")
    val phoneNumber: String,
    val password: String
)

data class LoginResponse (
    val message: String,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String
)