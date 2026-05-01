package com.fiky.lofo_app.data.api.services

import com.fiky.lofo_app.data.api.dto.user.UserProfileResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface UserService {
    @GET("users/me")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): UserProfileResponse
}