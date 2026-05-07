package com.fiky.lofo_app.data.api.repositories

import com.fiky.lofo_app.MyApp
import com.fiky.lofo_app.data.api.dto.auth.LoginRequest
import com.fiky.lofo_app.data.api.dto.auth.LoginResponse
import com.fiky.lofo_app.data.api.dto.auth.LogoutResponse
import com.fiky.lofo_app.data.api.dto.auth.RegisterRequest
import com.fiky.lofo_app.data.api.dto.auth.RegisterResponse
import com.fiky.lofo_app.data.api.retrofit.AuthPreferences
import com.fiky.lofo_app.data.api.services.ApiService
import com.fiky.lofo_app.data.locals.dataStore

class AuthRepository {
    private val dataStore = MyApp.instance.dataStore
    private val authPreferences: AuthPreferences = AuthPreferences(dataStore)

    suspend fun register(username: String, phoneNumber: String, password: String, address: String): RegisterResponse {
           return ApiService.authService.register(
               RegisterRequest(username, phoneNumber, password, address)
           )
    }

    suspend fun login(phoneNumber: String, password: String): LoginResponse {
        return ApiService.authService.login(
            LoginRequest(phoneNumber, password)
        )
    }

    suspend fun logout(): LogoutResponse {
        authPreferences.clearToken()
        return ApiService.authService.logout()
    }
}