package com.fiky.lofo_app.data.api.services

import com.fiky.lofo_app.data.api.dto.device_token.TokenRequest
import com.fiky.lofo_app.data.api.dto.device_token.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface DeviceTokenService {
    @POST("/api/device-tokens")
    suspend fun storeDeviceToken(
        @Body request: TokenRequest
    ): TokenResponse
}