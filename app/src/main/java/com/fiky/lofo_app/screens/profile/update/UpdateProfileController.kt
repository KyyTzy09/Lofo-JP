package com.fiky.lofo_app.screens.profile.update

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiky.lofo_app.data.api.repositories.AuthRepository
import com.fiky.lofo_app.data.api.repositories.UserRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

class UpdateProfileViewModel : ViewModel() {
    private val userRepo: UserRepository = UserRepository()

    var state by mutableStateOf(UpdateProfileState())
        private set

    init {
        viewModelScope.launch {
            try {
                val response = userRepo.getUserProfile()
                val userProfile = response.user

                state = state.copy(
                    name = userProfile.profile?.username?: "Memumat nama...",
                    description = userProfile.profile?.info?: "Memuat Info...",
                    address = userProfile?.profile?.address?: "Memuat Alamat..."
                    )
            } catch (e: Exception) {
                state = state.copy(error = e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun onNameChange(value: String) {
        state = state.copy(name = value)
    }

    fun onDescriptionChange(value: String) {
        state = state.copy(description = value)
    }

    fun onAddressChange(value: String) {
        state = state.copy(address = value)
    }

    fun updateProfile(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                if (state.name.isBlank()) throw Exception("Nama tidak boleh kosong")

                // Asumsi repository memiliki fungsi updateProfile
                 userRepo.updateUserProfile(state.name, state.description, state.address)

                state = state.copy(isSuccess = true)
                onSuccess()
            } catch (e: Exception) {
                state = state.copy(error = e.message ?: "Terjadi kesalahan")
                onError(e.message ?: "Terjadi kesalahan")
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}