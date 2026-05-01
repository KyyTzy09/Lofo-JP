package com.fiky.lofo_app.data.api.dto.user

import com.fiky.lofo_app.data.models.UserModel

data class UserProfileResponse(
    val message: String,
    val user: UserModel
)
