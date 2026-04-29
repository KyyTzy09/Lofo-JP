package com.fiky.lofo_app.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiky.lofo_app.MyApp
import com.fiky.lofo_app.data.api.retrofit.AuthPreferences
import com.fiky.lofo_app.data.locals.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authPreferences = AuthPreferences(MyApp.instance.dataStore)
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    init {
        observeSession()
    }
    private fun observeSession() {
        viewModelScope.launch {
            authPreferences.tokenFlow.collect { token ->
                android.util.Log.d("AUTH_CHECK", "Token yang terbaca: '$token'")
                _authState.value = if (!token.isNullOrEmpty()) {
                    AuthState.Authenticated
                } else {
                    AuthState.Unauthenticated
                }
            }
        }
    }
}