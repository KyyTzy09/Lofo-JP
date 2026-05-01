package com.fiky.lofo_app.screens.announcement.detail

import com.fiky.lofo_app.data.models.AnnouncementModel

data class AnnouncementDetailState(
    val announcement: AnnouncementModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)