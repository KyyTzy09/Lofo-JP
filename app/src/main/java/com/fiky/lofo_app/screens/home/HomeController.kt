package com.fiky.lofo_app.screens.home

import androidx.lifecycle.ViewModel
import com.fiky.lofo_app.screens.item.LostItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val _items = MutableStateFlow(
        listOf(
            LostItem(
                title = "iPhone 15 Pro",
                description = "Found near Gate B2",
                location = "Terminal 4",
                status = "65%"
            ),
            LostItem(
                title = "Leather Wallet",
                description = "Found 2h ago",
                location = "Lobby",
                status = "Awaiting Claim"
            )
        )
    )

    val items: StateFlow<List<LostItem>> = _items
}