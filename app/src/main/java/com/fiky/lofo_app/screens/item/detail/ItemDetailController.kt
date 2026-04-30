package com.fiky.lofo_app.screens.item.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiky.lofo_app.data.models.ItemModel
import com.fiky.lofo_app.data.models.ItemStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ItemDetailState(
    val item: ItemModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ItemDetailViewModel : ViewModel() {
    var state by mutableStateOf(ItemDetailState())
        private set

    fun getItemDetail(itemId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                // Simulasi fetching data
                // Dalam implementasi nyata, ini akan memanggil repository
                delay(1000)
                
                // Dummy data sesuai spesifikasi HTML "The Curated Concierge"
                val dummyItem = ItemModel(
                    itemId = itemId,
                    userId = "user_julian",
                    image = "https://lh3.googleusercontent.com/aida-public/AB6AXuByaF1sfd5liC1Qlut26vQHYAmAr46K9SlDBxGvza24aftPfDAkC3iO--CSO11ApYdif6oGKI3OCQxFwWIZks-FNCMOw-JdvXOUSL8PwFGLWznLQSoJhmeEkPC_HQkvbvgDsSprEmyW6MFB-sK22AheShtAtwduphRCGWj-QChnMcQNgab9_hZFFuO5n7AU5TALz0sCfLfEMWlfErQAdjkaA7QC_DdG3fJi0d5M7Nbthrfri7d_xyLeeAoTFrRdii0a2uNEjCAvMZ0S",
                    itemName = "Cognac Leather Portfolio Bag",
                    itemInfo = "Handcrafted from premium full-grain Italian leather, this portfolio bag features a minimalist silhouette designed for the modern professional. Includes a padded compartment for a 14-inch laptop, reinforced brass hardware, and a signature concierge tracking tag. The cognac patina develops uniquely over time, making it a truly personal accessory.",
                    status = ItemStatus.TERSEDIA,
                    qrUrl = "qr_id_portfolio_123",
                    user = null,
                    location = null,
                    createdAt = null,
                    updatedAt = null,
                    lastSeenAt = null
                )
                
                state = state.copy(item = dummyItem)
            } catch (e: Exception) {
                state = state.copy(error = e.message ?: "Terjadi kesalahan saat memuat detail")
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}
