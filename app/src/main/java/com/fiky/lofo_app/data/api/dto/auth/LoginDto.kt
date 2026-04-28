package com.fiky.lofo_app.data.api.dto.auth

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerializedName("phone_number")
    val phoneNumber: String,
    val password: String
)

@Serializable
data class LoginResponse (
    val message: String,
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("token_type")
    val tokenType: String
)