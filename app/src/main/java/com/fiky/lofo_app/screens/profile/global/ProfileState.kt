package com.fiky.lofo_app.screens.profile.global

data class UserState(
    val userId: String = "",
    val username: String = "",
    val phoneNumber: String = "",
    val profilePicture: String? = null,
    val isLoading: Boolean = false
)