package com.fiky.lofo_app.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val userId : String,
    @SerialName("phone_number")
    val phoneNumber : String,
    @SerialName("created_at")
    val createdAt : String,
    @SerialName("updated_at")
    val updatedAt : String,
    val profile : ProfileModel?,
    val items : List<ItemModel>
)
