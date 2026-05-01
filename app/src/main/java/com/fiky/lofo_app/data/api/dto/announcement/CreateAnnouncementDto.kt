package com.fiky.lofo_app.data.api.dto.announcement

import com.fiky.lofo_app.data.models.AnnouncementModel
import com.google.gson.annotations.SerializedName

data class CreateAnnouncementRequest(
    val title: String,
    val description: String,
    val location : String,
    @SerializedName("lost_at")
    val lostAt: String,
)

data class CreateAnnouncementResponse (
    val message: String,
    val data: AnnouncementModel
)