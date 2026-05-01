package com.fiky.lofo_app.screens.announcement.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiky.lofo_app.data.api.repositories.AnnouncementRepository
import kotlinx.coroutines.launch


class AnnouncementDetailViewModel : ViewModel() {
    private val announcementRepo = AnnouncementRepository()

    var state by mutableStateOf(AnnouncementDetailState())
        private set

    fun getDetail(id: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                val result = announcementRepo.GetAnnouncementDetail(id)
                state = state.copy(announcement = result.data)
            } catch (e: Exception) {
                state = state.copy(error = e.localizedMessage ?: "Gagal memuat detail")
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}