package com.fiky.lofo_app.data.api.services

import com.fiky.lofo_app.data.api.dto.auth.LoginRequest
import com.fiky.lofo_app.data.api.dto.auth.LoginResponse
import com.fiky.lofo_app.data.api.dto.auth.LogoutResponse
import com.fiky.lofo_app.data.api.dto.auth.RegisterRequest
import com.fiky.lofo_app.data.api.dto.auth.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("register")
    suspend fun register(
        @Body registerDto: RegisterRequest
    ): RegisterResponse

    @POST("login")
    suspend fun login(
        @Body loginDto: LoginRequest
    ): LoginResponse

    @POST("logout")
    suspend fun logout(): LogoutResponse
}