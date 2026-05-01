package com.fiky.lofo_app.data.api.services

import com.fiky.lofo_app.data.api.dto.item.BulkItemResponse
import com.fiky.lofo_app.data.api.dto.item.CreateItemResponse
import com.fiky.lofo_app.data.api.dto.item.ItemDetailResponse
import com.fiky.lofo_app.data.api.dto.item.UpdateItemRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ItemService {
    @GET("items")
    suspend fun getUserItems(
        @Query("status") status: String? = null,
        @Query("search") search: String? = null
    ): BulkItemResponse

    @POST("items")
    suspend fun createItem(
        @Part("item_name") itemName: String,
        @Part("item_info") itemInfo: String,
        @Part("image") image: MultipartBody.Part,
    ): CreateItemResponse

    //  Update image disini ntar

    @GET("items/{id}")
    suspend fun getItemDetail(
        @Path("id") id: String
    ): ItemDetailResponse

    @PATCH("items/{id}")
    suspend fun updateItem(
        @Path("id") id:String,
        @Body updateItemDto: UpdateItemRequest
    ): ItemDetailResponse

    @DELETE("items/{id}")
    suspend fun deleteItem(
        @Path("id") id: String
    ): ItemDetailResponse
}