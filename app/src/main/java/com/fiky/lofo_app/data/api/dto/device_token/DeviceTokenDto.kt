package com.fiky.lofo_app.data.api.dto.device_token

import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("token")
    val token: String
)

data class TokenResponse(
    @SerializedName("message")
    val message: String
)