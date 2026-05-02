package com.fiky.lofo_app.data.api.repositories

import com.fiky.lofo_app.MyApp
import com.fiky.lofo_app.data.api.dto.announcement.BulkAnnouncementDto
import com.fiky.lofo_app.data.api.dto.item.BulkItemResponse
import com.fiky.lofo_app.data.api.dto.user.UpdateProfileRequest
import com.fiky.lofo_app.data.api.dto.user.UpdateProfileResponse
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

    suspend fun getUserItems() : BulkItemResponse {
        val token = authPreferences.getToken()
        return ApiService.userService.getUserItems("Bearer $token")
    }

    suspend fun getUserAnnouncements() : BulkAnnouncementDto {
        val token = authPreferences.getToken()
        return ApiService.userService.getUserAnnouncements("Bearer $token")
    }

    suspend fun updateUserProfile(username: String, info: String?, address: String): UpdateProfileResponse {
        val token = authPreferences.getToken()
        val updateProfileDto = UpdateProfileRequest(username, info, address)
        return ApiService.userService.updateProfile(updateProfileDto, "Bearer $token")
    }
}