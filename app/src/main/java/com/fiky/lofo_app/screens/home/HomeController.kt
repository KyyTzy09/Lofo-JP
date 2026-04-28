package com.fiky.lofo_app.screens.home

import androidx.lifecycle.ViewModel
import com.fiky.lofo_app.data.models.ItemModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val _items = MutableStateFlow(
        listOf(
            ItemModel(
                itemId = "1",
                userId = "1",
                image = "https://res.cloudinary.com/finderapp/image/upload/v1764752239/item/Item-1764752236903.jpg",
                itemName = "Leather Wallet",
                itemInfo = "Found 2h ago",
                status = "TERSEDIA",
                qrUrl = null,
                user = null
            ),
            ItemModel(
                itemId = "2",
                userId = "1",
                image = "https://res.cloudinary.com/finderapp/image/upload/v1764752239/item/Item-1764752236903.jpg",
                itemName = "Leather Wallet",
                itemInfo = "Found 2h ago",
                status = "TERSEDIA",
                qrUrl = "http",
                user = null
            ),
            ItemModel(
                itemId = "3",
                userId = "1",
                image = "https://res.cloudinary.com/finderapp/image/upload/v1764752239/item/Item-1764752236903.jpg",
                itemName = "Mouse",
                itemInfo = "YAyaya",
                status = "HILANG",
                qrUrl = null,
                user = null
            )
        )
    )

    val items: StateFlow<List<ItemModel>> = _items
}
