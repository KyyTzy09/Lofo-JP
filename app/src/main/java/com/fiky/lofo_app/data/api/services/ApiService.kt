package com.fiky.lofo_app.data.api.services

import com.fiky.lofo_app.MyApp
import com.fiky.lofo_app.data.api.retrofit.ApiClient

object ApiService {
    val authService: AuthService by lazy {
        ApiClient.create(MyApp.instance).create(AuthService::class.java)
    }
}