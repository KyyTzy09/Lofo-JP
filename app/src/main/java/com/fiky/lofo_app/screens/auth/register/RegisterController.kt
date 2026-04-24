package com.fiky.lofo_app.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RegisterViewModel: ViewModel() {
        var state = RegisterState()
            private set

        fun onUsernameChange(value: String) {
            state = state.copy(username = value)
        }

        fun onPhoneChange(value: String) {
            state = state.copy(phone = value)
        }

        fun onPasswordChange(value: String) {
            state = state.copy(password = value)
        }

        fun register(onSuccess: () -> Unit) {
            viewModelScope.launch {
                state = state.copy(isLoading = true, error = null)

                try {
                    // simulasi API call (nanti ganti Retrofit)
                    delay(1500)

                    if (state.username.isBlank() || state.password.isBlank()) {
                        throw Exception("Field tidak boleh kosong")
                    }

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