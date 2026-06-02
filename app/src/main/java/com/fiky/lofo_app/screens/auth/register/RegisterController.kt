package com.fiky.lofo_app.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.fiky.lofo_app.data.api.repositories.AuthRepository
import org.json.JSONObject

class RegisterViewModel: ViewModel() {
    private var authRepo: AuthRepository = AuthRepository();

    var state by mutableStateOf(RegisterState())
        private set

    fun onUsernameChange(value: String) {
        state = state.copy(username = value)
    }

    fun onPhoneChange(value: String) {
        state = state.copy(phoneNumber = value)
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
            // 1. Validasi Lokal (Hadang di awal tanpa memicu try-catch)
            if (state.username.isBlank() || state.password.isBlank() || state.phoneNumber.isBlank()) {
                state = state.copy(error = "Field wajib diisi")
                return@launch // Stop proses di sini, gak usah lanjut ke bawah
            }

            if (!state.agreeTerms) {
                state = state.copy(error = "Anda harus menyetujui syarat dan ketentuan")
                return@launch // Stop proses juga
            }

            // 2. Jika validasi lolos, baru jalankan block network request yang rawan crash
            state = state.copy(isLoading = true, error = null)
            try {
                authRepo.register(state.username, state.phoneNumber, state.password, state.address)
                onSuccess()
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorBody ?: "{}")
                        .optString("message", "Terjadi kesalahan")
                } catch (ex: Exception) {
                    "Terjadi kesalahan"
                }
                state = state.copy(error = errorMessage)
            } catch (e: Exception) {
                // Berjaga-jaga kalau ada error network / IO biasa selain HTTP error
                state = state.copy(error = e.localizedMessage ?: "Terjadi kesalahan jaringan")
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}
