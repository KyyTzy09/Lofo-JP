package com.fiky.lofo_app.utils

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import com.fiky.lofo_app.composables.ToastType

object ToastHelper {
    suspend fun show(
        state: SnackbarHostState,
        title: String,
        description: String,
        type: ToastType
    ) {
        state.showSnackbar(
            "$title|$description|${type.name}",
            duration = SnackbarDuration.Short)
    }
}

// Penggunaan:
// ToastHelper.show(snackbarHostState, "Error", "Password salah!", ToastType.ERROR)