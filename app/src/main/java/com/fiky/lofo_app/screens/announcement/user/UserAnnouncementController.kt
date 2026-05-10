package com.fiky.lofo_app.screens.announcement.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiky.lofo_app.data.api.repositories.AnnouncementRepository
import com.fiky.lofo_app.data.api.repositories.UserRepository
import com.fiky.lofo_app.data.models.AnnouncementModel
import com.fiky.lofo_app.data.models.AnnouncementStatus
import kotlinx.coroutines.launch


class UserAnnouncementViewModel : ViewModel() {
    private val userRepo = UserRepository()

    var state by mutableStateOf(UserAnnouncementState())
        private set

    init {
        fetchUserAnnouncements()
    }

    fun fetchUserAnnouncements() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                // Implementasi repo.getUserAnnouncements() sesuai API kamu
                val result = userRepo.getUserAnnouncements()
                state = state.copy(
                    announcements = result.data,
                    filteredAnnouncements = result.data,
                    isLoading = false
                )
            } catch (e: Exception) {
                state = state.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun filterByStatus(status: String) {
        val filtered = when (status) {
            "Pending" -> state.announcements.filter { it.status == AnnouncementStatus.PENDING }
            "Resolved" -> state.announcements.filter { it.status == AnnouncementStatus.CLOSED }
            else -> state.announcements
        }
        state = state.copy(selectedStatus = status, filteredAnnouncements = filtered)
    }
}