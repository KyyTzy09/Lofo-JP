package com.fiky.lofo_app.screens.item.user

import com.fiky.lofo_app.data.models.ItemModel

data class ItemUserState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val items: List<ItemModel> = emptyList()
)
