package com.fiky.lofo_app.data.api.dto.announcement

import com.fiky.lofo_app.data.models.AnnouncementModel
import com.fiky.lofo_app.data.models.AnnouncementStatus

data class UpdateAnnouncementRequest(
    val status: AnnouncementStatus
)

data class UpdateAnnouncementResponse(
    val message: String,
    val data: AnnouncementModel
)