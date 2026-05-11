package com.fiky.lofo_app.screens.item.user

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.fiky.lofo_app.data.api.repositories.UserRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiky.lofo_app.data.models.ItemModel
import kotlinx.coroutines.launch
import org.json.JSONObject

class UserItemViewModel : ViewModel() {
    private val userRepo: UserRepository = UserRepository()
    var state by mutableStateOf(ItemUserState())
        private set

    fun onSearchQueryChange(query: String) {
        state = state.copy(searchQuery = query)
    }

    fun fetchUserItems() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                val response = userRepo.getUserItems()
                state = state.copy(items = response.data)
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorBody ?: "{}").optString("message", "Gagal memuat data")
                } catch (ex: Exception) { "Gagal memuat data" }
                state = state.copy(error = errorMessage)
            } catch (e: Exception) {
                state = state.copy(error = e.localizedMessage ?: "Terjadi kesalahan")
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}