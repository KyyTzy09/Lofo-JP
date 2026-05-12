package com.fiky.lofo_app.screens.item.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fiky.lofo_app.data.models.ItemModel
import com.fiky.lofo_app.data.models.ItemStatus

@Composable
fun UserItemCard(
    item: ItemModel,
    onCardClick: () -> Unit,
    onUpdate: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    val isLost = item.status == ItemStatus.HILANG

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Flat design lebih modern untuk dark mode
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Bagian Gambar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                AsyncImage(
                    model = item.image,
                    contentDescription = item.itemName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Surface(
                    modifier = Modifier.padding(12.dp),
                    color = if (isLost) MaterialTheme.colorScheme.error else Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Text(
                        text = if (isLost) "HILANG" else "DITEMUKAN",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White, // Putih murni agar terbaca di atas warna gelap/merah
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nama & Info
            Text(
                text = item.itemName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary // Warna Ungu Utama
            )
            Text(
                text = item.itemInfo,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant, // Abu-abu terang
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Baris Tombol
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onCardClick,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLost) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = if (isLost) "LACAK" else "DETAIL",
                        fontWeight = FontWeight.Bold,
                        color = if (isLost) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onPrimary
                    )
                }

                IconButton(
                    onClick = { onUpdate(item.itemId) },
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary // Ikon Ungu
                    )
                }
                IconButton(
                    onClick = { onDelete(item.itemId) },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Red, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceContainer // Ikon Ungu
                    )
                }
            }
        }
    }
}