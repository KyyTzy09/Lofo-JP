package com.fiky.lofo_app.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class RegisterViewModel: ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set

    fun onFullNameChange(value: String) {
        state = state.copy(fullName = value)
    }

    fun onUsernameChange(value: String) {
        state = state.copy(username = value)
    }

    fun onEmailChange(value: String) {
        state = state.copy(email = value)
    }

    fun onPhoneChange(value: String) {
        state = state.copy(phone = value)
    }

    fun onAddressChange(value: String) {
        state = state.copy(address = value)
    }

    fun onPasswordChange(value: String) {
        state = state.copy(password = value)
    }

    fun onConfirmPasswordChange(value: String) {
        state = state.copy(confirmPassword = value)
    }

    fun onToggleTerms(value: Boolean) {
        state = state.copy(agreeTerms = value)
    }

    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            try {
                // simulasi API call
                delay(1500)

                if (state.username.isBlank() || state.password.isBlank() || state.phone.isBlank()) {
                    throw Exception("Field wajib diisi")
                }

                if (state.password != state.confirmPassword) {
                    throw Exception("Password tidak cocok")
                }

                if (!state.agreeTerms) {
                    throw Exception("Anda harus menyetujui syarat dan ketentuan")
                }

                onSuccess()
            } catch (e: Exception) {
                state = state.copy(error = e.message)
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}
