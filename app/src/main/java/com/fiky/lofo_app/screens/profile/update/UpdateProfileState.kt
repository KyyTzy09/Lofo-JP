package com.fiky.lofo_app.screens.profile.update

data class UpdateProfileState(
    val name: String = "",
    val description: String = "",
    val address: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)