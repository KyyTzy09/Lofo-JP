package com.fiky.lofo_app.screens.item.update

data class UpdateItemState(
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val isLoading: Boolean = false,
    val isFetching: Boolean = false,
    val error: String? = null
)