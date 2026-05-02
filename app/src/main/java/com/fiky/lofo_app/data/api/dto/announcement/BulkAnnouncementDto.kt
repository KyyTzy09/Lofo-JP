package com.fiky.lofo_app.data.api.dto.announcement

import com.fiky.lofo_app.data.models.AnnouncementModel

data class BulkAnnouncementDto(
    val message: String,
    val data: List<AnnouncementModel>
)
