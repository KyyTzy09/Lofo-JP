package com.fiky.lofo_app.screens.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fiky.lofo_app.data.models.ItemModel
import com.fiky.lofo_app.data.models.ItemStatus

@Composable
fun ItemCard(navController: NavController, item: ItemModel) {
    Card(
        onClick = {
            navController.navigate("announcement_detail/dsdishdus")
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row (modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = "https://res.cloudinary.com/finderapp/image/upload/v1764752239/item/Item-1764752236903.jpg",
                contentDescription = "gambar",
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = item.itemName,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = item.itemInfo,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (item.status == ItemStatus.TERSEDIA) "Tersedia" else "Hilang",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}