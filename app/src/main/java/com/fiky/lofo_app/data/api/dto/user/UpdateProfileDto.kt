package com.fiky.lofo_app.data.api.dto.user

import com.fiky.lofo_app.data.models.ProfileModel

data class UpdateProfileRequest(
    val username: String,
    val info: String?,
    val address: String
)

data class UpdateProfileResponse(
    val message: String,
    val user: ProfileModel
)
