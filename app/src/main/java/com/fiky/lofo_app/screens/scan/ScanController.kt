package com.fiky.lofo_app.screens.scan

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiky.lofo_app.data.api.repositories.ItemRepository
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanViewModel : ViewModel() {
    val state = ScanState()
    private val itemRepo: ItemRepository = ItemRepository()

    @SuppressLint("MissingPermission")
    fun onQrCodeDetected(
        context: Context,
        code: String,
        onCodeScanned: (String, Double?, Double?) -> Unit,
        onError: (String) -> Unit
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationRequest = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        try {
            if (state.lastScannedCode != code) {
                state.lastScannedCode = code

                fusedLocationClient.getCurrentLocation(locationRequest, null)
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            // 1. Langsung picu redirect di UI
                            onCodeScanned(code, location.latitude, location.longitude)
                            // 2. Jalankan proses update di background coroutine milik ViewModel
                            updateItemLocationInBackground(code, location.latitude, location.longitude)
                        } else {
                            onCodeScanned(code, null, null)
                        }
                    }
                    .addOnFailureListener { exception ->
                        onCodeScanned(code, null, null)
                        onError(exception.localizedMessage ?: "Gagal mendeteksi lokasi")
                    }
            }
        } catch (e: Exception) {
            onError(e.localizedMessage ?: "Gagal mendeteksi lokasi")
        }
    }

    private fun updateItemLocationInBackground(itemId: String, lat: Double, lon: Double) {
        // Menggunakan viewModelScope bawaan ViewModel Architecture Component
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    itemRepo.UpdateItemLocation(itemId, lat, lon)
                }
                println("Log: Berhasil memperbarui lokasi di background via ViewModel.")
            } catch (e: Exception) {
                println("Log Error: ${e.localizedMessage}")
            }
        }
    }

    fun updatePermission(isGranted: Boolean) {
        state.isCameraPermissionGranted = isGranted
    }
}