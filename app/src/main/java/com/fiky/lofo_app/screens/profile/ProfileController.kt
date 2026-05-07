package com.fiky.lofo_app.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.fiky.lofo_app.data.api.repositories.AuthRepository
import com.fiky.lofo_app.data.api.repositories.UserRepository
import com.fiky.lofo_app.data.models.UserModel
import kotlinx.coroutines.launch
import org.json.JSONObject


class ProfileViewModel : ViewModel() {
    private val userRepo = UserRepository()

    var state by mutableStateOf(ProfileState())
        private set

    init {
        getProfile()
    }

    fun getProfile() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                 val response = userRepo.getUserProfile()
                 state = state.copy(user = response.user)
            } catch (e: Exception) {
                state = state.copy(error = e.message ?: "Gagal mengambil data")
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            // Logika clear session/token
            onSuccess()
        }
    }
}
