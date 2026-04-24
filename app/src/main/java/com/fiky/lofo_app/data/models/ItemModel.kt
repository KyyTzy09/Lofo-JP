package com.fiky.lofo_app.data.models

data class ItemModel(
    val itemId : String,
    val userId : String,
    val image : String,
    val itemName : String,
    val itemInfo : String,
    val status: String,
    val qrUrl : String,
    val user : UserModel
)
