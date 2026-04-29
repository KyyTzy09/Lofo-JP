package com.fiky.lofo_app.data.models

import com.google.gson.annotations.SerializedName

data class UserModel(
    val userId : String,
    @SerializedName("phone_number")
    val phoneNumber : String,
    @SerializedName("created_at")
    val createdAt : String,
    @SerializedName("updated_at")
    val updatedAt : String,
    val profile : ProfileModel?,
    val items : List<ItemModel>
)
