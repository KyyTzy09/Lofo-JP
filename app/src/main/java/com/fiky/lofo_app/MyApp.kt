package com.fiky.lofo_app

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApp: Application() {
    companion object {
        lateinit var instance: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        FirebaseApp.initializeApp(this)
    }
}