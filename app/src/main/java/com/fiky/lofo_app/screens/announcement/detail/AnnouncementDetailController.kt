package com.fiky.lofo_app.screens.announcement.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiky.lofo_app.data.api.dto.announcement.UpdateAnnouncementRequest
import com.fiky.lofo_app.data.api.dto.announcement.UpdateAnnouncementStatusRequest
import com.fiky.lofo_app.data.api.repositories.AnnouncementRepository
import com.fiky.lofo_app.data.models.AnnouncementStatus
import kotlinx.coroutines.launch


class AnnouncementDetailViewModel : ViewModel() {
    private val announcementRepo = AnnouncementRepository()

    var state by mutableStateOf(AnnouncementDetailState())
        private set

    fun updateAnnouncement(
        id: String,
        title: String,
        location: String,
        lostAt: String, // String ISO / UTC murni ("yyyy-MM-dd'T'HH:mm:ss'Z'")
        description: String
    ) {
        viewModelScope.launch {
            // Set loading biar tombol di modal menampilkan CircularProgressIndicator
            state = state.copy(isLoading = true, error = null)
            try {
                val requestBody = UpdateAnnouncementRequest(
                    title = title,
                    location = location,
                    lostAt = lostAt,
                    description = description,
                    itemId = state.announcement?.itemId
                )

                val result = announcementRepo.UpdateAnnouncement(id, requestBody)
                state = state.copy(announcement = result.data)

            } catch (e: Exception) {
                state = state.copy(error = e.localizedMessage ?: "Gagal memperbarui pengumuman")
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }

    fun markAsCompleted(id: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                val result = announcementRepo.UpdateAnnouncementStatus(
                    id, UpdateAnnouncementStatusRequest(
                        AnnouncementStatus.CLOSED
                    )
                )
                state = state.copy(announcement = result.data)
            } catch (e: Exception) {
                state = state.copy(error = e.localizedMessage ?: "Gagal mengubah status")
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }

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

        fun contactOwner(context: Context, phoneNumber: String) {
            try {
                // 1. Bersihkan semua karakter non-angka
                var cleanNumber = phoneNumber.replace(Regex("[^0-9]"), "")

                // 2. FIX BUG LOGIKA: Jika nomor diawali angka '0', ubah paksa jadi '62'
                if (cleanNumber.startsWith("0")) {
                    cleanNumber = "62" + cleanNumber.substring(1)
                }

                // 3. Susun URL Deep Link resmi WhatsApp
                val url = "https://wa.me/$cleanNumber?text=${Uri.encode("Halo, saya ingin bertanya mengenai barang anda yang hilang.")}"
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(url)
                    // Tambahkan flag ini karena kamu memulai Activity dari luar konteks Activity murni (Context aplikasi)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                // 4. FIX CRASH: Cek dulu apakah ada aplikasi (Browser/WA) yang bisa menerima intent ini
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    // Fallback jika device bener-bener gak punya browser atau WA (sangat jarang terjadi)
                    android.widget.Toast.makeText(context, "Tidak ada aplikasi yang mendukung untuk membuka tautan", android.widget.Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    fun deleteAnnouncement(id: String, onDeleteSuccess: () -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                announcementRepo.DeleteAnnouncement(id)
                onDeleteSuccess()
            } catch (e: Exception) {
                state = state.copy(error = e.localizedMessage ?: "Gagal menghapus pengumuman")
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}