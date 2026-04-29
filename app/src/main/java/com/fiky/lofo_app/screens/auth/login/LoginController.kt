package com.fiky.lofo_app.screens.auth.login

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
import com.fiky.lofo_app.data.api.retrofit.AuthPreferences
import com.fiky.lofo_app.data.locals.dataStore
import org.json.JSONObject

class LoginViewModel: ViewModel() {
    private var authRepo: AuthRepository = AuthRepository();
    private var authPreferences: AuthPreferences = AuthPreferences(MyApp.instance.dataStore);

    var state by mutableStateOf(LoginState())
        private set

    fun onPhoneChange(value: String) {
        state = state.copy(phone = value)
    }

    fun onPasswordChange(value: String) {
        state = state.copy(password = value)
    }

    fun login(onSuccess: () -> Unit) {
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
}