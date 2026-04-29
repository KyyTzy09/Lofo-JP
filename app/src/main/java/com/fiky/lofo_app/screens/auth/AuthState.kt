package com.fiky.lofo_app.screens.auth

sealed class AuthState {
    object Loading: AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
}