package com.fiky.lofo_app.screens.item.create

import android.net.Uri


data class CreateItemState(
    val itemName: String = "",
    val itemDescription: String = "",
    val selectedImageUri: Uri? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
