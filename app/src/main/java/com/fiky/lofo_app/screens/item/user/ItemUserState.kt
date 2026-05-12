package com.fiky.lofo_app.screens.item.user

import com.fiky.lofo_app.data.models.ItemModel

data class ItemUserState(
    val items: List<ItemModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)
