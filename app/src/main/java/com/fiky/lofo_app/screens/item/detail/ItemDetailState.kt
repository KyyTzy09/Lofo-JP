package com.fiky.lofo_app.screens.item.detail

import com.fiky.lofo_app.data.models.ItemModel

data class ItemDetailState(
    val item: ItemModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)