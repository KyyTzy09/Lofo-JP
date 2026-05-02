package com.fiky.lofo_app.screens.announcement.create

import com.fiky.lofo_app.data.models.ItemModel

data class CreateAnnouncementState(
    val title: String = "",
    val description: String = "",
    val lastLocation: String = "",
    val selectedItemId: String? = null,
    val selectedItemName: String? = null,
    val dateLost: String = "",
    val isLoading: Boolean = false,
    val isFetchingItem: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val items: List<ItemModel> = emptyList()
)
