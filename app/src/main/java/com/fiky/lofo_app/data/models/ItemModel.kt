package com.fiky.lofo_app.data.models

import com.google.gson.annotations.SerializedName

data class ItemModel(
    val itemId : String,
    @SerializedName("user_id")
    val userId : String,
    val image : String,
    @SerializedName("item_name")
    val itemName : String,
    @SerializedName("item_info")
    val itemInfo : String,
    val status: ItemStatus,
    @SerializedName("qr_url")
    val qrUrl : String?,
//    @SerialName("last_seen_at")
//    val lastSeenAt : ,
//    @SerialName("created_at")
//    val createdAt : String,
//    @SerialName("updated_at")
//    val updatedAt : String,
    val user : UserModel?
)

enum class ItemStatus {
    HILANG,
    TERSEDIA
}