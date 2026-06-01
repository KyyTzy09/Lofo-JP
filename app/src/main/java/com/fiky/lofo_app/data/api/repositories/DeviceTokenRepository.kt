package com.fiky.lofo_app.data.api.repositories

import com.fiky.lofo_app.data.api.dto.device_token.TokenRequest
import com.fiky.lofo_app.data.api.services.ApiService

class DeviceTokenRepository {
    suspend fun storeDeviceToken(token: String) {
        val request = TokenRequest(token)
        try {
            val response = ApiService.deviceTokenService.storeDeviceToken(request)
            println("FCM Token Berhasil Di-Sync ke Server: $token")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
