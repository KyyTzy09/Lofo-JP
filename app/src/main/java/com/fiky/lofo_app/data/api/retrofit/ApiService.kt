package com.fiky.lofo_app.data.api.retrofit

import com.fiky.lofo_app.data.api.services.AuthService

object ApiService {
    val authService: AuthService = ApiClient.retrofit.create(AuthService::class.java)
}