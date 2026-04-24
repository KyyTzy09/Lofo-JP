package com.fiky.lofo_app.screens.auth.register

data class RegisterState(
    val username: String = "",
    val phone: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)