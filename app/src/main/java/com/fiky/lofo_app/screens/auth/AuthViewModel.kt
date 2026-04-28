package com.fiky.lofo_app.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {

    var authState = mutableStateOf<AuthState>(AuthState.Loading)

    init {

    }

//
//    private fun checkSession() {
//        viewModelScope.launch {
//            val token = getTokenFromStorage()
//
//            authState = if (token != null) {
//                AuthState.Authenticated
//            } else {
//                AuthState.Unauthenticated
//            }
//        }
//    }
}