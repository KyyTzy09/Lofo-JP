package com.fiky.lofo_app.data.models

data class UserModel(
    val userId : String,
    val phoneNumber : String,
    val createdAt : String,
    val updatedAt : String,
    val profile : ProfileModel?,
    val items : List<ItemModel>
)


