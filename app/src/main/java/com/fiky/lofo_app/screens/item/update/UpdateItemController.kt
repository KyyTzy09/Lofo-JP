package com.fiky.lofo_app.screens.item.update

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiky.lofo_app.data.api.repositories.ItemRepository
import kotlinx.coroutines.launch


class UpdateItemViewModel : ViewModel() {
    private val itemRepo = ItemRepository()

    var state by mutableStateOf(UpdateItemState())
        private set

    fun onNameChange(value: String) = run { state = state.copy(name = value) }
    fun onDescriptionChange(value: String) = run { state = state.copy(description = value) }


// Load data Item saat  viewModel di inisialisasi
    fun loadItemData(itemId: String) {
        viewModelScope.launch {
            state = state.copy(isFetching = true)
            try {
                val response = itemRepo.GetItemDetail(itemId)
                val item = response.data;
                state = state.copy(
                    name = item.itemName,
                    description = item.itemInfo ?: "",
                    imageUrl = item.image ?: "",
                    isFetching = false
                )
            } catch (e: Exception) {
                state = state.copy(error = e.message, isFetching = false)
            }
        }
    }

    fun updateItem(itemId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                if (state.name.isBlank()) throw Exception("Nama barang wajib diisi")

//                Tembak API Update
                itemRepo.UpdateItem(itemId, state.name, state.description)

                state = state.copy(isLoading = false)
                onSuccess()
            } catch (e: Exception) {
                state = state.copy(isLoading = false)
                onError(e.message ?: "Gagal memperbarui barang")
            }
        }
    }
}