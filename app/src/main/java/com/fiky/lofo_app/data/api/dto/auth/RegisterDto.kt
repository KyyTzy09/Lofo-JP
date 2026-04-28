package com.fiky.lofo_app.data.api.dto.auth

import com.fiky.lofo_app.data.models.UserModel

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val address: String
)

data class RegisterResponse (
    val message: String,
    val data: UserModel
)