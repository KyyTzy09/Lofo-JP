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

data class LostItem(
    val title: String,
    val description: String,
    val location: String,
    val status: String
)

@Composable
fun ItemCard(navController: NavController, item: LostItem) {
    Card(
        onClick = {
//            navController.navigate("detail/${item.title}")
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
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.location,
                    style = MaterialTheme.typography.labelSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.status,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}