package com.fiky.lofo_app.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    var state by mutableStateOf(ProfileState())
        private set

    fun logout() {
        // Implement logout logic here
    }
}
