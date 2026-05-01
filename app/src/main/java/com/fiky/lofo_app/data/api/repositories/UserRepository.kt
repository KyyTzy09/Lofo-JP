package com.fiky.lofo_app.data.api.repositories

import com.fiky.lofo_app.MyApp
import com.fiky.lofo_app.data.api.dto.user.UserProfileResponse
import com.fiky.lofo_app.data.api.retrofit.AuthPreferences
import com.fiky.lofo_app.data.api.services.ApiService
import com.fiky.lofo_app.data.locals.dataStore

class UserRepository {
    private val dataStore = MyApp.instance.dataStore
    private val authPreferences: AuthPreferences = AuthPreferences(dataStore)

    suspend fun getUserProfile(): UserProfileResponse {
        val token = authPreferences.getToken()
        return ApiService.userService.getUserProfile("Bearer $token")
    }
}