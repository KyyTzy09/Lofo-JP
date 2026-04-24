package com.fiky.lofo_app.screens.item

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


data class CreateItemState(
    val itemName: String = "",
    val itemDescription: String = "",
    val selectedImageUri: Uri? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
