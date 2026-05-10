package com.fiky.lofo_app.screens.announcement.user

import com.fiky.lofo_app.data.models.AnnouncementModel

data class UserAnnouncementState(
    val announcements: List<AnnouncementModel> = emptyList(),
    val filteredAnnouncements: List<AnnouncementModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedStatus: String = "All"
)