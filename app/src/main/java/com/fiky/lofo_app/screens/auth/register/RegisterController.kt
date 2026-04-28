package com.fiky.lofo_app.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.fiky.lofo_app.data.api.repositories.AuthRepository

class RegisterViewModel: ViewModel() {
    private var authRepo: AuthRepository = AuthRepository();

    var state by mutableStateOf(RegisterState())
        private set

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

    fun onToggleTerms(value: Boolean) {
        state = state.copy(agreeTerms = value)
    }

    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            try {
                if (state.username.isBlank() || state.password.isBlank() || state.phone.isBlank()) {
                    throw Exception("Field wajib diisi")
                }

                if (!state.agreeTerms) {
                    throw Exception("Anda harus menyetujui syarat dan ketentuan")
                }

                authRepo.register(state.username, state.phone, state.password, state.address)
                onSuccess()
            } catch (e: Exception) {
                state = state.copy(error = e.message)
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}
