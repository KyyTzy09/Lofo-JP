package com.fiky.lofo_app.screens.auth.login

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import coil.network.HttpException
import com.fiky.lofo_app.MyApp
import com.fiky.lofo_app.data.api.repositories.AuthRepository
import com.fiky.lofo_app.data.api.repositories.DeviceTokenRepository
import com.fiky.lofo_app.data.api.retrofit.AuthPreferences
import com.fiky.lofo_app.data.api.services.ApiService
import com.fiky.lofo_app.data.locals.dataStore
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject

class LoginViewModel: ViewModel() {
    private var authRepo: AuthRepository = AuthRepository();
    private var authPreferences: AuthPreferences = AuthPreferences(MyApp.instance.dataStore);
    private var deviceTokenRepository: DeviceTokenRepository = DeviceTokenRepository();


    var state by mutableStateOf(LoginState())
        private set

    fun onPhoneChange(value: String) {
        state = state.copy(phone = value)
    }

    fun onPasswordChange(value: String) {
        state = state.copy(password = value)
    }

    fun login(context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            try {
                if (state.phone.isBlank() || state.password.isBlank()) {
                    throw Exception("Field tidak boleh kosong")
                }

                val result = authRepo.login(state.phone, state.password)
                authPreferences.saveToken(result.accessToken)
                android.util.Log.d("AUTH_CHECK", "Token yang terbaca: '${result.accessToken}'")
                // sukses
                syncDeviceToken(context)
                onSuccess()
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorBody ?: "{}")
                        .optString("message", "Terjadi kesalahan")
                } catch (ex: Exception) {
                    "Terjadi kesalahan"
                }
                state = state.copy(error = errorMessage)

            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }

    fun syncDeviceToken(context: android.content.Context) {
        if (FirebaseApp.getApps(context).isEmpty()) {
            val options = com.google.firebase.FirebaseOptions.Builder()
                .setApiKey("ISI_DENGAN_CURRENT_KEY_DARI_JSON")
                .setApplicationId("ISI_DENGAN_MOBILESDK_APP_ID_DARI_JSON")
                .setProjectId("lofo-app-d785c")
                .build()

            com.google.firebase.FirebaseApp.initializeApp(context, options)
        }

        // Sekarang panggil FirebaseMessaging pasti lancar jaya tanpa crash
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                viewModelScope.launch {
                    try {
                        deviceTokenRepository.storeDeviceToken(token)
                        println("FCM Token Berhasil Di-Sync ke Server: $token")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}