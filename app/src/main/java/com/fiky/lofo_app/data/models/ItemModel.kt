package com.fiky.lofo_app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemModel(
    val itemId : String,
    @SerialName("user_id")
    val userId : String,
    val image : String,
    @SerialName("item_name")
    val itemName : String,
    @SerialName("item_info")
    val itemInfo : String,
    val status: ItemStatus,
    @SerialName("qr_url")
    val qrUrl : String?,
//    @SerialName("last_seen_at")
//    val lastSeenAt : ,
    @SerialName("created_at")
    val createdAt : String,
    @SerialName("updated_at")
    val updatedAt : String,
    val user : UserModel?
)

enum class ItemStatus {
    HILANG,
    TERSEDIA
}