package com.fiky.lofo_app.screens.auth.register

data class RegisterState(
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val agreeTerms: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)