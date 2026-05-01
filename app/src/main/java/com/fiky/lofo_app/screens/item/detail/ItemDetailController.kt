package com.fiky.lofo_app.screens.item.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiky.lofo_app.data.api.repositories.ItemRepository
import com.fiky.lofo_app.data.models.ItemModel
import com.fiky.lofo_app.data.models.ItemStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ItemDetailViewModel : ViewModel() {
    private val itemRepo: ItemRepository = ItemRepository()
    var state by mutableStateOf(ItemDetailState())
        private set

    fun getItemDetail(itemId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                val item = itemRepo.GetItemDetail(itemId)
                state = state.copy(item = item.data)
            } catch (e: Exception) {
                state = state.copy(error = e.message ?: "Terjadi kesalahan saat memuat detail")
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}
