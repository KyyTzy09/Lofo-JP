package com.fiky.lofo_app.screens.scan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ScanState {
    var isFlashlightOn by mutableStateOf(false)
    var lastScannedCode by mutableStateOf<String?>(null)
    var isCameraPermissionGranted by mutableStateOf(false)
}
