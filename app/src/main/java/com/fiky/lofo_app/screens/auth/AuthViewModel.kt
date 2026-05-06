package com.fiky.lofo_app.screens.auth

import android.util.Log
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
import retrofit2.HttpException

class AuthViewModel : ViewModel() {
    private val dataStore = MyApp.instance.dataStore
    private val authPreferences = AuthPreferences(dataStore)
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    private val userRepo: UserRepository = UserRepository()

    init {
        observeSession()
    }

    private fun observeSession() {
        viewModelScope.launch {
            val token = authPreferences.getToken()

            if (token.isNullOrEmpty()) {
                _authState.value = AuthState.Unauthenticated
                return@launch
            }

            try {
                val response = userRepo.getUserProfile()

                if (response.user != null) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Unauthenticated
                }
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    _authState.value = AuthState.Unauthenticated
                    authPreferences.clearToken()
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }
}