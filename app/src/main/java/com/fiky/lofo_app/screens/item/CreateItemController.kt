package com.fiky.lofo_app.screens.item

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CreateItemViewModel: ViewModel() {
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

    fun create(onSuccess: () -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            try {
                // simulasi API call (nanti ganti Retrofit)
                delay(1500)

                if (state.itemName.isBlank() || state.selectedImageUri === null) {
                    throw Exception("Field tidak boleh kosong")
                }

                // sukses
                onSuccess()
            } catch (e: Exception) {
                state = state.copy(error = e.message)
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}