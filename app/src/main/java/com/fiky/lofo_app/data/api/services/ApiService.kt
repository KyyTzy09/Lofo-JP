package com.fiky.lofo_app.data.api.services

import com.fiky.lofo_app.MyApp
import com.fiky.lofo_app.data.api.retrofit.ApiClient

object ApiService {
    val userService: UserService by lazy {
        ApiClient.create(MyApp.instance).create(UserService::class.java)
    }

    val authService: AuthService by lazy {
        ApiClient.create(MyApp.instance).create(AuthService::class.java)
    }

    val itemService: ItemService by lazy {
        ApiClient.create(MyApp.instance).create(ItemService::class.java)
    }

    val announcementService: AnnouncementService by lazy {
        ApiClient.create(MyApp.instance).create(AnnouncementService::class.java)
    }
}