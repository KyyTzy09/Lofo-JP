package com.fiky.lofo_app.screens.announcement.user

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fiky.lofo_app.composables.ToastType
import com.fiky.lofo_app.screens.voice.VoiceCommandModal
import com.fiky.lofo_app.utils.ToastHelper
import kotlinx.coroutines.launch
import java.util.jar.Manifest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAnnouncementScreen(
    onBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCreate: () -> Unit,
    viewModel: UserAnnouncementViewModel = viewModel(),
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val state = viewModel.state
    val selectorList = listOf(
        SelectorList(
            label = "Semua",
            value = "All"
        ),
        SelectorList(
            label = "Aktif",
            value = "Pending"
        ),
        SelectorList(
            label = "Selesai",
            value = "Resolved"
        )
    )
    var showVoiceModal by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult (
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showVoiceModal = true
        } else {
            scope.launch {
                ToastHelper.show(snackbarHostState, "Izin Dibutuhkan!!", "Berikan izin untuk menggunakan microphone", ToastType.SUCCESS)
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    val permissionCheckResult = ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.RECORD_AUDIO
                    )

                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        showVoiceModal = true
                    } else {
                        // Munculkan popup izin sistem
                        permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .navigationBarsPadding() // Otomatis naik mengikuti sistem navigasi/bottom bar
                    .padding(bottom = 16.dp) // Jarak tambahan agar tidak menempel pas
            ) {
                Icon(Icons.Default.AutoAwesome, null)
                Spacer(Modifier.width(8.dp))
                Text("AI Voice")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("Pengumuman Saya",
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Announcement, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        if (showVoiceModal) {
            VoiceCommandModal(
                onDismiss = { showVoiceModal = false },
                onSend = { transcript, isConnected ->
                    viewModel.CreateWithVoice(
                        text = transcript,
                        connectItem = isConnected,
                        onSuccess = {
                            scope.launch {
                                ToastHelper.show(snackbarHostState, "Sukses", "Pengumuman berhasil dibuat", ToastType.SUCCESS)
                            }
                            showVoiceModal = false
                            viewModel.fetchUserAnnouncements()
                        },
                        onError = {
                            scope.launch {
                                ToastHelper.show(snackbarHostState, "Gagal", it, ToastType.ERROR)
                            }
                        }
                    )
                }
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Section
            item(span = { GridItemSpan(2) }) {
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(
                        "Pengumuman",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        "Lihat semua pengumuman anda",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Filter Chips
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectorList.forEach { status ->
                        FilterChip(
                            selected = state.selectedStatus == status.value,
                            onClick = { viewModel.filterByStatus(status.value) },
                            label = { Text(status.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            border = null,
                            shape = CircleShape
                        )
                    }
                }
            }

            // Announcement Cards
            items(state.filteredAnnouncements) { announcement ->
                UserAnnouncementCard(announcement) { onNavigateToDetail(announcement.announcementId) }
            }

            // Add New Placeholder
            item {
                Surface(
                    onClick = onNavigateToCreate,
                    modifier = Modifier.aspectRatio(0.7f).fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier.size(50.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceContainer
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(Modifier.height(12.dp))
                        Text("Tambah", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
