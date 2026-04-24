package com.fiky.lofo_app.screens.scan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class ScanController(
    val state: ScanState,
    private val onCodeScanned: (String) -> Unit
) {
    fun toggleFlashlight() {
        state.isFlashlightOn = !state.isFlashlightOn
    }

    fun onQrCodeDetected(code: String) {
        if (state.lastScannedCode != code) {
            state.lastScannedCode = code
            onCodeScanned(code)
        }
    }

    fun updatePermission(isGranted: Boolean) {
        state.isCameraPermissionGranted = isGranted
    }
}

@Composable
fun rememberScanController(
    state: ScanState = remember { ScanState() },
    onCodeScanned: (String) -> Unit = {}
): ScanController {
    return remember(state) {
        ScanController(state, onCodeScanned)
    }
}
