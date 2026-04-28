package com.fiky.lofo_app.data.api.repositories

import com.fiky.lofo_app.data.api.dto.auth.LoginRequest
import com.fiky.lofo_app.data.api.dto.auth.LoginResponse
import com.fiky.lofo_app.data.api.dto.auth.RegisterRequest
import com.fiky.lofo_app.data.api.dto.auth.RegisterResponse
import com.fiky.lofo_app.data.api.retrofit.ApiService

class AuthRepository {
    suspend fun register(username: String, email: String, password: String, address: String): RegisterResponse {
           return ApiService.authService.register(
               RegisterRequest(username, email, password, address)
           )
    }

    suspend fun login(phoneNumber: String, password: String): LoginResponse {
        return ApiService.authService.login(
            LoginRequest(phoneNumber, password)
        )
    }
}