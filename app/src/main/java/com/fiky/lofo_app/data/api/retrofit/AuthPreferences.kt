package com.fiky.lofo_app.data.api.retrofit

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AuthPreferences(private val dataStore: DataStore<Preferences>) {

    companion object {
        val TOKEN_KEY = stringPreferencesKey("accessToken")
    }

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    suspend fun getToken(): String? {
        return dataStore.data.first()[TOKEN_KEY]
    }

    val tokenFlow: Flow<String?> = dataStore.data.map {
        it[TOKEN_KEY]
    }

    suspend fun clearToken() {
        dataStore.edit {
            it.remove(TOKEN_KEY)
        }
    }
}