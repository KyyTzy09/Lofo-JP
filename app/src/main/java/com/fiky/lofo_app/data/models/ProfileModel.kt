package com.fiky.lofo_app.data.models

data class ProfileModel(
    val userId  : String,
    val username: String,
    val info    : String?,
    val address:  String?,
    val user : UserModel
)
