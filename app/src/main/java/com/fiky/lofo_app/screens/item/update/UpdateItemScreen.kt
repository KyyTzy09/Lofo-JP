package com.fiky.lofo_app.screens.item.update

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fiky.lofo_app.composables.ToastType
import com.fiky.lofo_app.screens.profile.update.UpdateInputFields
import com.fiky.lofo_app.utils.ToastHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateItemScreen(
    itemId: String,
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: UpdateItemViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val state = viewModel.state
    val scrollState = rememberScrollState()

    LaunchedEffect(itemId) {
        viewModel.loadItemData(itemId)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Update Item", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isFetching) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.primary)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // --- ITEM IMAGE SECTION (Read Only with Overlay Style) ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(4f / 3f)
                            .clip(RoundedCornerShape(32.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(32.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    ) {
                        AsyncImage(
                            model = state.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Floating Badge (Vault Style)
                        Surface(
                            modifier = Modifier.align(Alignment.BottomStart).padding(20.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.8f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Verified, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("VAULTED ITEM", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 1.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- FORM FIELDS ---
                    UpdateInputFields(
                        label = "Item Name",
                        value = state.name,
                        onValueChange = viewModel::onNameChange,
                        placeholder = "Enter item name",
                        icon = Icons.Default.Inventory2
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    UpdateInputFields(
                        label = "Item Description",
                        value = state.description,
                        onValueChange = viewModel::onDescriptionChange,
                        placeholder = "Enter item description",
                        icon = Icons.Default.Description,
                        isTextArea = true
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- ACTION BUTTON ---
                    Button(
                        onClick = {
                            viewModel.updateItem(
                                itemId = itemId,
                                onSuccess = {
                                    scope.launch {
                                        ToastHelper.show(snackbarHostState, "Update Sukses", "Barang berhasil diperbarui!", ToastType.SUCCESS)
                                        onBack()
                                    }
                                },
                                onError = { errorMsg ->
                                    scope.launch {
                                        ToastHelper.show(snackbarHostState, "Gagal", errorMsg, ToastType.ERROR)
                                    }
                                }
                            )
                        },
                        enabled = !state.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimaryContainer)
                        } else {
                            Text("Save Changes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                            Spacer(Modifier.width(12.dp))
                            Icon(Icons.Default.CheckCircle, null)
                        }
                    }

                    Text(
                        "Last synced to secure vault",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}