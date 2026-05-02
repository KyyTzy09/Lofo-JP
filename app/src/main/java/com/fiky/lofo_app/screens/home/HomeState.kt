package com.fiky.lofo_app.screens.home

import com.fiky.lofo_app.data.models.AnnouncementModel

data class HomeState(
    val announcements: List<AnnouncementModel> = emptyList(),
    val pendingAnnouncements: List<AnnouncementModel> = emptyList(),
    val closedAnnouncements: List<AnnouncementModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

