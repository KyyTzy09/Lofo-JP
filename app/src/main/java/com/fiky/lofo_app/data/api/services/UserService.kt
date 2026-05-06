package com.fiky.lofo_app.data.api.services

import com.fiky.lofo_app.data.api.dto.announcement.BulkAnnouncementDto
import com.fiky.lofo_app.data.api.dto.item.BulkItemResponse
import com.fiky.lofo_app.data.api.dto.user.UpdateProfileRequest
import com.fiky.lofo_app.data.api.dto.user.UpdateProfileResponse
import com.fiky.lofo_app.data.api.dto.user.UserProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface UserService {
    @GET("users/profile")
    suspend fun getUserProfile(
         @Header("Accept") accept: String = "application/json"
    ): UserProfileResponse

    @GET("users/items")
    suspend fun getUserItems(
        @Header("Authorization") token: String,
        @Header("Accept") accept: String = "application/json"
    ): BulkItemResponse

    @GET("users/announcements")
    suspend fun getUserAnnouncements(
        @Header("Authorization") token: String,
        @Header("Accept") accept: String = "application/json"
    ):  BulkAnnouncementDto

    @PATCH("users/profile")
    suspend fun updateProfile(
        @Body updateProfileDto: UpdateProfileRequest,
        @Header("Authorization") token: String,
    ): UpdateProfileResponse
}