package com.fiky.lofo_app.screens.auth.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.fiky.lofo_app.data.api.repositories.AuthRepository

class LoginViewModel: ViewModel() {
    private var authRepo: AuthRepository = AuthRepository();

    var state by mutableStateOf(LoginState())
        private set

    fun onPhoneChange(value: String) {
        state = state.copy(phone = value)
    }

    fun onPasswordChange(value: String) {
        state = state.copy(password = value)
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            try {
                if (state.phone.isBlank() || state.password.isBlank()) {
                    throw Exception("Field tidak boleh kosong")
                }

                val result = authRepo.login(state.phone, state.password)
                // sukses
                onSuccess()
            } catch (e: Exception) {
                state = state.copy(error = e.message)
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}