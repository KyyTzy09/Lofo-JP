package com.fiky.lofo_app.data.api.repositories

import com.fiky.lofo_app.data.api.dto.item.BulkItemResponse
import com.fiky.lofo_app.data.api.dto.item.CreateItemResponse
import com.fiky.lofo_app.data.api.dto.item.ItemDetailResponse
import com.fiky.lofo_app.data.api.dto.item.UpdateItemRequest
import com.fiky.lofo_app.data.api.services.ApiService
import okhttp3.MultipartBody

class ItemRepository {
    suspend fun GetAllItems(): BulkItemResponse {
        return ApiService.itemService.getItems()
    }

    suspend fun CreateItem(itemName: String, itemInfo: String, image: MultipartBody.Part): CreateItemResponse {
        return ApiService.itemService.createItem(
            itemName,
            itemInfo,
            image
        )
    }

    suspend fun GetItemDetail(itemId: String): ItemDetailResponse {
        return ApiService.itemService.getItemDetail(itemId)
    }

    suspend fun UpdateItem(itemId: String, itemName: String, itemInfo: String, status: String): ItemDetailResponse {
        return ApiService.itemService.updateItem(
            itemId,
            UpdateItemRequest(itemName, itemInfo, status)
        )
    }

    suspend fun DeleteItem(itemId: String): ItemDetailResponse {
        return ApiService.itemService.deleteItem(itemId)
    }
}