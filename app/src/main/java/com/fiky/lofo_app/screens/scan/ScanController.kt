package com.fiky.lofo_app.screens.scan

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.fiky.lofo_app.data.api.repositories.ItemRepository
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanController (
    val state: ScanState,
    private val onCodeScanned: (String, Double?, Double?) -> Unit,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val scope: CoroutineScope
) {
    private val itemRepo: ItemRepository = ItemRepository()

    @SuppressLint("MissingPermission")
    fun onQrCodeDetected(code: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val locationRequest = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY) // Memaksa akurasi tinggi (GPS)
            .build()

        if (state.lastScannedCode != code) {
            state.lastScannedCode = code


            fusedLocationClient.getCurrentLocation(locationRequest, null)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        onCodeScanned(code, location.latitude, location.longitude)
                        updateItemLocation(code, location.latitude, location.longitude, onSuccess, onError)
                    } else {
                        // Kemungkinan null di sini sangat kecil, kecuali GPS HP mati total
                        onCodeScanned(code, null, null)
                        onSuccess()
                    }
                }
                .addOnFailureListener { exception ->
                    onCodeScanned(code, null, null)
                    onError(exception.localizedMessage ?: "Gagal mendeteksi lokasi")
                }
        }
    }

    fun updateItemLocation(itemId: String, lat: Double, lon: Double, onSuccess: () -> Unit, onError: (String) -> Unit) {
        scope.launch {
            try {
                withContext(Dispatchers.IO) {
                    itemRepo.UpdateItemLocation(itemId, lat, lon)
                }
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Gagal memperbarui lokasi")
            }
        }
    }

    fun updatePermission(isGranted: Boolean) {
        state.isCameraPermissionGranted = isGranted
    }
}

@Composable
fun rememberScanController(
    context: Context = LocalContext.current,
    state: ScanState = remember { ScanState() },
    onCodeScanned: (String, Double?, Double?) -> Unit = { _, _, _ -> }
): ScanController {
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    val scope = rememberCoroutineScope()

    return remember(state, fusedLocationClient) {
        ScanController(state, onCodeScanned, fusedLocationClient, scope)
    }
}