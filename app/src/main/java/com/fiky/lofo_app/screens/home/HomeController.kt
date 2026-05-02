package com.fiky.lofo_app.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiky.lofo_app.data.api.repositories.AnnouncementRepository
import com.fiky.lofo_app.data.models.AnnouncementModel
import com.fiky.lofo_app.data.models.AnnouncementStatus
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    // Referensi repository (sesuaikan dengan DI atau Repo Anda)
     private val announcementRepo = AnnouncementRepository()

    var state by mutableStateOf(HomeState())
        private set

    init {
        fetchAnnouncements()
    }

    fun onSearchQueryChange(query: String) {
        state = state.copy(searchQuery = query)
    }

    fun fetchAnnouncements() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                 val all = announcementRepo.GetAllAnnouncements()

                state = state.copy(
                    pendingAnnouncements = all.data.filter { it.status == AnnouncementStatus.PENDING },
                    closedAnnouncements = all.data.filter { it.status == AnnouncementStatus.CLOSED },
                    isLoading = false
                )
            } catch (e: Exception) {
                state = state.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun searchAnnouncements(query: String) {
        viewModelScope.launch {
            try {
                val filtered = state.pendingAnnouncements.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true)
                }

                state = state.copy(
                    pendingAnnouncements = filtered.filter { it.status == AnnouncementStatus.PENDING },
                    closedAnnouncements = filtered.filter { it.status == AnnouncementStatus.CLOSED }
                )
            }
            catch (e: Exception) {
                state = state.copy(error = e.message)
            }
        }
    }
}