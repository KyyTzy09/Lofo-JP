package com.fiky.lofo_app.screens.profile.global

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiky.lofo_app.data.api.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GlobalProfileViewModel : ViewModel() {
    private val userRepo = UserRepository()
    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            try {
            _userState.update { it.copy(isLoading = true) }
             val response = userRepo.getUserProfile()
            val user = response.user
            _userState.update {
                it.copy(
                    userId = user.userId,
                    username = user.profile?.username ?: "",
                    phoneNumber = user.phoneNumber,
                    profilePicture = user.profile?.avatar ?: "https://i.pinimg.com/736x/8b/16/7a/8b167af653c2399dd93b952a48740620.jpg",
                    address = user.profile?.address ?: "",
                    description = user.profile?.info ?: "",
                    isLoading = false
                )
            }
            } catch (e: Exception) {
                _userState.update { it.copy(isLoading = false) }
            }
        }
    }
}