package com.fiky.lofo_app.data.models

import com.google.gson.annotations.SerializedName

data class AnnouncementModel(
    val announcementId: String,
    val title: String,
    val description: String,
    val location: String,
    val status: AnnouncementStatus,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("item_id")
    val itemId: String?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val user: UserModel?
)

enum class AnnouncementStatus {
    PENDING,
    CLOSED
}