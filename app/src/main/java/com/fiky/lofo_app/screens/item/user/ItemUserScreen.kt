package com.fiky.lofo_app.screens.item.user


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiky.lofo_app.data.models.ItemStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserItemScreen(
    viewModel: UserItemViewModel,
    onNavigateToDetail: (String) -> Unit,
    onAddItem: () -> Unit,
    onUpdateItem: (String) -> Unit,
) {
    val state = viewModel.state

    // --- STATE BARU UNTUK KONFIRMASI DELETE ---
    var itemToDeleteId by remember { mutableStateOf<String?>(null) }
    val showDeleteDialog = remember(itemToDeleteId) { itemToDeleteId != null }

    LaunchedEffect(Unit) {
        viewModel.fetchUserItems()
    }

    // --- DIALOG KONFIRMASI (MODAL) ---
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { itemToDeleteId = null }, // Tutup modal jika klik luar
            icon = { Icon(Icons.Default.DeleteForever, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = {
                Text(
                    text = "Hapus Barang?",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    text = "Apakah Anda yakin ingin menghapus barang ini? Tindakan ini tidak dapat dibatalkan.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        itemToDeleteId?.let { id ->
                            // Panggil fungsi delete di viewModel kamu
                            viewModel.deleteItem(
                                itemId = id,
                                onDeleteSuccess = {
                                    // Setelah sukses, tutup modal
                                    itemToDeleteId = null
                                }
                            )
                        }
                        itemToDeleteId = null // Tutup modal setelah sukses trigger
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus", color = MaterialTheme.colorScheme.onError)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { itemToDeleteId = null }
                ) {
                    Text("Batal")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(28.dp)
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddItem,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        },
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(1), // Bisa kamu ubah ke 2 kolom nanti sesuai komentar kodenmu
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 80.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- HEADER SECTION ---
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(
                        text = "Barang Saya",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Total: ${state.items.size}",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        // Indicator barang hilang
                        val lostCount = state.items.count { it.status == ItemStatus.HILANG }
                        if (lostCount > 0) {
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "$lostCount Hilang",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                }
            }

            // --- EMPTY STATE ---
            if (state.items.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(modifier = Modifier.fillMaxSize().padding(top = 100.dp), contentAlignment = Alignment.Center) {
                        Text("Belum ada barang yang diupload", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // --- LIST ITEMS ---
            items(state.items) { item ->
                UserItemCard(
                    item = item,
                    onCardClick = { onNavigateToDetail(item.itemId) },
                    onUpdate = onUpdateItem,
                    // LANGKAH CERDIK: Titipkan id barang ke state saat tombol tong sampah diklik
                    onDelete = { itemToDeleteId = item.itemId }
                )
            }
        }
    }
}


@Composable
fun StatChip(label: String, count: String, selected: Boolean) {
    Surface(
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = label, color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface, fontSize = 12.sp)
            Spacer(Modifier.width(8.dp))
            Text(text = count, fontWeight = FontWeight.Bold, color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary, fontSize = 12.sp)
        }
    }
}