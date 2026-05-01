package com.fiky.lofo_app.data.api.dto.announcement

import com.fiky.lofo_app.data.models.AnnouncementModel

data class PendingAnnouncements(
    val message: String,
    val data: List<AnnouncementModel>
)
