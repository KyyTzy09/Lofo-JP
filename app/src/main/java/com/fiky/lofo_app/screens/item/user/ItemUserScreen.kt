package com.fiky.lofo_app.screens.item.user

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserItemScreen(
    viewModel: UserItemViewModel,
    onNavigateToDetail: (String) -> Unit,
    onAddItem: () -> Unit
) {
    val state = viewModel.state

    LaunchedEffect(Unit) {
        viewModel.fetchUserItems()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddItem,
                containerColor = Color(0xFF14002F),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item", Modifier.size(32.dp))
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(1), // Default list, bisa diubah ke 2 untuk tablet
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Section (Hero)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    Text(
                        text = "Barang Saya",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF14002F)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = Color(0xFFECDCFF), // secondary-fixed
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "${state.items.size} Barang",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6D4EA2)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // List Items
            items(state.items) { item ->
                UserItemCard(
                    item = item,
                    onClick = { onNavigateToDetail(item.itemId) }
                )
            }
        }
    }
}