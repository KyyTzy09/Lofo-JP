package com.fiky.lofo_app.data.models

import com.google.gson.annotations.SerializedName

data class ItemLocation(
    val locationId: String,
    val latitude: Double,
    val longitude: Double,
    @SerializedName("item_id")
    val itemId: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)
