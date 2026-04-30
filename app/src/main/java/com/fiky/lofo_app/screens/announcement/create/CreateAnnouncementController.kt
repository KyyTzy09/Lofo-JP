package com.fiky.lofo_app.screens.announcement.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CreateAnnouncementViewModel : ViewModel() {
    // Diasumsikan repository sudah ada
    // private val announcementRepo = AnnouncementRepository()

    var state by mutableStateOf(CreateAnnouncementState())
        private set

    fun onTitleChange(value: String) { state = state.copy(title = value) }
    fun onDescriptionChange(value: String) { state = state.copy(description = value) }
    fun onLocationChange(value: String) { state = state.copy(lastLocation = value) }
    fun onItemSelected(value: String?) { state = state.copy(selectedItem = value) }
    fun onDateChange(value: String) { state = state.copy(dateLost = value) }

    fun createAnnouncement(onSuccess: () -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                if (state.title.isBlank() || state.lastLocation.isBlank() || state.dateLost.isBlank()) {
                    throw Exception("Judul, Lokasi, dan Tanggal wajib diisi")
                }

                // announcementRepo.create(...)

                onSuccess()
            } catch (e: Exception) {
                state = state.copy(error = e.message ?: "Terjadi kesalahan")
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}