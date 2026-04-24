package com.fiky.lofo_app.screens.auth.login

data class LoginState(
    val phone: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
