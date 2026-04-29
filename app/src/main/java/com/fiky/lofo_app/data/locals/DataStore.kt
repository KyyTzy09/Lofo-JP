package com.fiky.lofo_app.data.locals

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore("auth")
