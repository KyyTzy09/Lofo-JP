package com.fiky.lofo_app.data.api.dto.item

import com.fiky.lofo_app.data.models.ItemModel
import com.google.gson.annotations.SerializedName


data class UpdateItemRequest(
    @SerializedName("item_name")
    val itemName: String,
    @SerializedName("item_info")
    val itemInfo: String,
    val status: String
)

data class CreateItemResponse (
    val message: String,
    val data: ItemModel
)

data class ItemDetailResponse(
    val message: String,
    val data: ItemModel
)

data class BulkItemResponse(
    val message: String,
    val data: List<ItemModel>
)
