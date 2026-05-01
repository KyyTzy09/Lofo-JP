package com.fiky.lofo_app.screens.item.create

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.fiky.lofo_app.MyApp
import com.fiky.lofo_app.data.api.repositories.ItemRepository
import com.fiky.lofo_app.utils.UrIToMultipartHelper
import com.fiky.lofo_app.utils.UrIToMultipartHelper.uploadImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CreateItemViewModel: ViewModel() {
    private val itemRepo: ItemRepository = ItemRepository()

    var state by mutableStateOf(CreateItemState())
        private set

    fun onNameChange(value: String) {
        state = state.copy(itemName = value)
    }

    fun onDescriptionChange(value: String) {
        state = state.copy(itemDescription = value)
    }

    fun onImageSelected(uri: Uri?) {
        state = state.copy(selectedImageUri = uri)
    }

    fun create(onSuccess: (itemId: String) -> Unit, onError: (message: String) -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            try {
                if (state.itemName.isBlank()) {
                    throw Exception("Nama tidak boleh kosong")
                }

                if (state.selectedImageUri === null) {
                    throw Exception("Gambar tidak boleh kosong")
                }

                val image = uploadImage(MyApp.instance,  state.selectedImageUri!!, "image")
                val created = itemRepo.CreateItem(
                    itemName = state.itemName,
                    itemInfo = state.itemDescription,
                    image = image
                )

                onSuccess(created?.data?.itemId ?: "")
            } catch (e: Exception) {
                state = state.copy(error = e.message)
                onError(e.message ?: "Terjadi Kesalahan")
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}