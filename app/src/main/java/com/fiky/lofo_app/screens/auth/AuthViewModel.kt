package com.fiky.lofo_app.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiky.lofo_app.MyApp
import com.fiky.lofo_app.data.api.repositories.UserRepository
import com.fiky.lofo_app.data.api.retrofit.AuthPreferences
import com.fiky.lofo_app.data.locals.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    private val userRepo: UserRepository = UserRepository()
    init {
        observeSession()
    }
    private fun observeSession() {
        viewModelScope.launch {
            val response = userRepo.getUserProfile()
            val session = response.user
            _authState.value = if (session !== null) {
                AuthState.Authenticated
            } else {
                AuthState.Unauthenticated
            }
        }
    }
}