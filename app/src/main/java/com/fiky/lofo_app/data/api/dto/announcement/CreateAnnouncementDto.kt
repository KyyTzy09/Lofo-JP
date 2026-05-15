package com.fiky.lofo_app.data.api.dto.announcement

import com.fiky.lofo_app.data.models.AnnouncementModel
import com.google.gson.annotations.SerializedName

data class CreateAnnouncementRequest(
    val title: String,
    val description: String,
    val location : String,
    @SerializedName("lost_at")
    val lostAt: String,
    @SerializedName("item_id")
    val itemId: String? = null
)

data class CreateAnnouncementResponse (
    val message: String,
    val data: AnnouncementModel
)

data class CreateAnnouncementWithVoiceRequest (
    val text: String,
    @SerializedName("connect_item")
    val connectItem : Boolean,
)

data class CreateAnnouncementWithVoiceResponse(
    val message: String,
    val data: AnnouncementModel
)