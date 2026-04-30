package com.fiky.lofo_app.screens.announcement.create

data class CreateAnnouncementState(
    val title: String = "",
    val description: String = "",
    val lastLocation: String = "",
    val selectedItem: String? = null,
    val dateLost: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
