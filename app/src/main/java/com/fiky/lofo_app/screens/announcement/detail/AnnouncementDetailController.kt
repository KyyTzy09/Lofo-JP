package com.fiky.lofo_app.screens.announcement.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.fiky.lofo_app.data.api.repositories.AnnouncementRepository
import com.fiky.lofo_app.data.models.AnnouncementModel
import kotlinx.coroutines.launch

data class AnnouncementDetailState(
    val announcement: AnnouncementModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class AnnouncementDetailViewModel : ViewModel() {
    // Inisialisasi Repo (Sesuaikan dengan arsitektur DI kamu)
//    private val repository = AnnouncementRepository()

    var state by mutableStateOf(AnnouncementDetailState())
        private set

    fun getDetail(id: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                // Asumsi repository punya fungsi getAnnouncementById
//                val result = repository.getAnnouncementById(id)
//                state = state.copy(announcement = result)
            } catch (e: Exception) {
                state = state.copy(error = e.localizedMessage ?: "Gagal memuat detail")
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}