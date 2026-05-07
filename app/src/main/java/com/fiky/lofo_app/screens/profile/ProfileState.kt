package com.fiky.lofo_app.screens.profile

import com.fiky.lofo_app.data.models.UserModel

data class ProfileState(
    val user: UserModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)