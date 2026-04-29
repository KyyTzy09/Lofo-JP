package com.fiky.lofo_app.screens.auth.register

data class RegisterState(
    val username: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val password: String = "",
    val agreeTerms: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)