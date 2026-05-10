package com.fiky.lofo_app.screens.scan

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.fiky.lofo_app.MyApp
import com.fiky.lofo_app.data.api.repositories.ItemRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
    private val itemRepo: ItemRepository = ItemRepository();

    @SuppressLint("MissingPermission")
    fun onQrCodeDetected(code: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (state.lastScannedCode != code) {
            state.lastScannedCode = code

            // Ambil lokasi dulu sebelum eksekusi callback
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    onCodeScanned(code, location.latitude, location.longitude)
                    updateItemLocation(code, location.latitude, location.longitude, onSuccess, onError)
                } else {
                    onCodeScanned(code, null, null)
                }
            }.addOnFailureListener {
                onCodeScanned(code, null, null)
            }
        }
    }

    fun updateItemLocation(itemId: String, lat: Double, lon: Double, onSuccess: () -> Unit, onError: (String) -> Unit) {
        scope.launch {
            try {
                // Jalankan API call di background thread (IO)
                withContext(Dispatchers.IO) {
                    itemRepo.UpdateItemLocation(itemId, lat, lon)
                }
                // Kembali ke UI thread untuk memanggil callback sukses
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
    context: Context = LocalContext.current, // Ambil context untuk location client
    state: ScanState = remember { ScanState() },
    onCodeScanned: (String, Double?, Double?) -> Unit = { _, _, _ -> }
): ScanController {
    // Inisialisasi FusedLocationProviderClient
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val scope = rememberCoroutineScope()

    return remember(state, fusedLocationClient) {
        ScanController(state, onCodeScanned, fusedLocationClient, scope)
    }
}