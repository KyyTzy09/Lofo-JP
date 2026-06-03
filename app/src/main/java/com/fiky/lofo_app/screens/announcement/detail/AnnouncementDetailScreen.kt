package com.fiky.lofo_app.screens.announcement.detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.fiky.lofo_app.MyApp
import com.fiky.lofo_app.data.models.AnnouncementModel
import com.fiky.lofo_app.data.models.AnnouncementStatus
import com.fiky.lofo_app.screens.announcement.update.UpdateAnnouncementModal
import com.fiky.lofo_app.screens.home.StatusBadge
import com.fiky.lofo_app.screens.profile.global.GlobalProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementDetailScreen(
    announcementId: String,
    onBack: () -> Unit,
    onNavigateToItemDetail: (itemId: String) -> Unit, // <--- TAMBAHAN PARAMETER NAVIGASI BARU
    viewModel: AnnouncementDetailViewModel = viewModel(),
    profileViewModel: GlobalProfileViewModel
) {
    val state = viewModel.state
    val userState by profileViewModel.userState.collectAsState()
    val context = MyApp.instance
    val scrollState = rememberScrollState()

    var showUpdateModal by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val isOwner by remember(userState, state.announcement) {
        derivedStateOf {
            val currentUserId = userState.userId
            val ownerId = state.announcement?.userId
            currentUserId.isNotEmpty() && currentUserId == ownerId
        }
    }

    LaunchedEffect(announcementId) {
        viewModel.getDetail(announcementId)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Announcement Details", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    if (isOwner && state.announcement?.status == AnnouncementStatus.PENDING) {
                        IconButton(onClick = { showUpdateModal = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Announcement", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                tonalElevation = 8.dp
            ) {
                Button(
                    onClick = {
                        if (isOwner && state.announcement?.status == AnnouncementStatus.PENDING) {
                            viewModel.markAsCompleted(announcementId)
                        } else {
                            viewModel.contactOwner(
                                context = MyApp.instance,
                                phoneNumber = state.announcement?.user?.phoneNumber ?: ""
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = Color.White
                    ),
                ) {
                    if (isOwner) {
                        Icon(Icons.Default.CheckCircle, null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (state.announcement?.status == AnnouncementStatus.PENDING) "Tandai Selesai" else "Pengumuman Berakhir",
                            fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                    } else {
                        Icon(Icons.Default.ChatBubble, null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (state.announcement?.status == AnnouncementStatus.PENDING) "Hubungi Pemilik" else "Pengumuman Berakhir",
                            fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                    }
                }
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primaryContainer)
            }
        } else if (state.announcement != null) {
            val data = state.announcement

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                // =========================================================================
                // IMPROVISASI 1: LOGIC BANNER GAMBAR / IMAGE PLACEHOLDER KREATIF
                // =========================================================================
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(32.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    if (data.item != null) {
                        // Jika terhubung dengan barang, tampilkan foto aslinya
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(data.item.image)
                                .decoderFactory(SvgDecoder.Factory())
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // JURUS KUNCI: Tampilan bento placeholder jika pengumuman polos dari AI Voice
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                modifier = Modifier.size(64.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Category,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "Laporan Tanpa Fisik",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Pengumuman ini dibuat tanpa menautkan ke barang terdaftar.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(
                                    horizontal = 24.dp,
                                    vertical = 16.dp
                                )
                            )
                        }
                    }

                    // Badge status tetep nangkring di pojok kiri atas banner
                    Box(modifier = Modifier.padding(16.dp)) {
                        StatusBadge(data.status)
                    }
                }

                // --- BENTO SECTION 2: TITLE & COMPLETE STATS ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(Modifier.padding(24.dp)) {
                        Text(
                            text = "PENGUMUMAN BARANG",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = data.title,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )

                        Spacer(Modifier.height(24.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(Modifier.height(24.dp))

                        InfoRow(
                            icon = Icons.Default.AccessTimeFilled,
                            label = "Tanggal Kehilangan:",
                            value = data.lostAt.take(10)
                        )

                        Spacer(Modifier.height(16.dp))

                        InfoRow(
                            icon = Icons.Default.Schedule,
                            label = "Diumumkan Pada:",
                            value = data.createdAt.take(10)
                        )

                        Spacer(Modifier.height(16.dp))

                        InfoRow(
                            icon = Icons.Default.Update,
                            label = "Terakhir Diperbarui:",
                            value = data.updatedAt.take(10)
                        )
                        Spacer(Modifier.height(16.dp))

                        InfoRow(
                            icon = Icons.Default.LocationOn,
                            label = "Lokasi Terakhir:",
                            value = data.location
                        )
                    }
                }

                // --- DESCRIPTION SECTION ---
                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Text(
                        "DESCRIPTION",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = data.description,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            lineHeight = 28.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    )
                }

                val hasLinkedItem = data.item != null
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (hasLinkedItem) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.08f)
                        else MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (hasLinkedItem) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    ),
                    // Jika terhubung item, card-nya jadi hidup dan bisa diclick
                    onClick = { if (hasLinkedItem) onNavigateToItemDetail(data.itemId ?: "") },
                    enabled = hasLinkedItem
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (hasLinkedItem) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = if (hasLinkedItem) Icons.Default.QrCodeScanner else Icons.Default.LayersClear,
                                contentDescription = null,
                                tint = if (hasLinkedItem) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (hasLinkedItem) "Lihat Detail Barang" else "Tidak Terhubung dengan Barang",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (hasLinkedItem) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = if (hasLinkedItem) "Pengumuman ini terhubung dengan barang terdaftar"
                                else "Pengumuman independen, tidak terikat stiker QR Code.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (hasLinkedItem) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // --- OWNER PROFILE CARD & DELETE BUTTON SECTION ---
                data.user?.let { user ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(user.profile?.avatar ?: "")
                                            .decoderFactory(SvgDecoder.Factory())
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                Spacer(Modifier.width(16.dp))

                                Column(Modifier.weight(1f)) {
                                    Text(
                                        text = if (isOwner) "Anda" else user.profile?.username ?: "Anonymous User",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        "Pemilik Barang",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        if (isOwner) {
                            OutlinedButton(
                                onClick = { showDeleteDialog = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                ),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Hapus Pengumuman Ini", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(130.dp))
            }

            if (showUpdateModal) {
                UpdateAnnouncementModal(
                    announcement = data,
                    onDismiss = { showUpdateModal = false },
                    onUpdateSubmit = { updatedTitle, updatedLocation, updatedDate, updatedDescription ->
                        viewModel.updateAnnouncement(
                            id = announcementId,
                            title = updatedTitle,
                            location = updatedLocation,
                            lostAt = updatedDate,
                            description = updatedDescription
                        )
                        showUpdateModal = false
                    },
                    isUpdating = state.isLoading
                )
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    icon = { Icon(Icons.Default.DeleteForever, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                    title = {
                        Text(
                            text = "Hapus Pengumuman?",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    text = {
                        Text(
                            text = "Apakah Anda yakin ingin menghapus pengumuman ini? Tindakan ini akan menghapus laporan dari sistem LoFo secara permanen.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.deleteAnnouncement(
                                    id = announcementId,
                                    onDeleteSuccess = {
                                        showDeleteDialog = false
                                        onBack()
                                    }
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Hapus", color = MaterialTheme.colorScheme.onError)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDeleteDialog = false }
                        ) {
                            Text("Batal")
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(28.dp)
                )
            }
        }
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f),
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.padding(10.dp)
            )
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}