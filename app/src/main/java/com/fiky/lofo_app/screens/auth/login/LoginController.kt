package com.fiky.lofo_app.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LoginViewModel: ViewModel() {
    var state = LoginState()
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
                // simulasi API call (nanti ganti Retrofit)
                delay(1500)

                if (state.phone.isBlank() || state.password.isBlank()) {
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