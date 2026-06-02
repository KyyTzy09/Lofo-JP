package com.fiky.lofo_app.data.api.dto.announcement

import com.fiky.lofo_app.data.models.AnnouncementModel
import com.fiky.lofo_app.data.models.AnnouncementStatus
import com.google.gson.annotations.SerializedName

data class UpdateAnnouncementRequest(
    val title: String,
    val description: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("item_id")
    val itemId : String ?= null,
    @SerializedName("lost_at")
    val lostAt: String
)

data class UpdateAnnouncementStatusRequest(
    val status: AnnouncementStatus
)

data class UpdateAnnouncementResponse(
    val message: String,
    val data: AnnouncementModel
)