package com.fiky.lofo_app.screens.announcement.detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fiky.lofo_app.MyApp
import com.fiky.lofo_app.data.models.AnnouncementStatus
import com.fiky.lofo_app.screens.home.StatusBadge
import com.fiky.lofo_app.screens.profile.global.GlobalProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementDetailScreen(
    announcementId: String,
    onBack: () -> Unit,
    viewModel: AnnouncementDetailViewModel = viewModel(),
    profileViewModel: GlobalProfileViewModel
    ) {
    val state = viewModel.state
    val userState by profileViewModel.userState.collectAsState()

    val scrollState = rememberScrollState()
    val isOwner by remember(userState, state.announcement) {
        derivedStateOf {
            val currentUserId = userState.userId
            val ownerId = state.announcement?.userId
            // Debugging: Tambahkan log untuk memastikan ID mana yang kosong
            // println("Current: $currentUserId, Owner: $ownerId")
            currentUserId.isNotEmpty() && currentUserId == ownerId
        }
    }

    LaunchedEffect(announcementId) {
        viewModel.getDetail(announcementId)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // Sesuai @Color background dark
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Announcement Details", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent, // Biar menyatu dengan background
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
                        if (isOwner && state.announcement?.status === AnnouncementStatus.PENDING) {
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
                            if (state.announcement?.status === AnnouncementStatus.PENDING) "Tandai Selesai" else "Pengumuman Berakhir",
                            fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                    } else {
                        Icon(Icons.Default.ChatBubble, null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (state.announcement?.status === AnnouncementStatus.PENDING) "Hubungi Pemilik" else "Pengumuman Berakhir",
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
                if (data.item != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(32.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                    ) {
                        AsyncImage(
                            model = data.item.image,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Status Badge Overlay
                        Box(modifier = Modifier.padding(16.dp)) {
                            StatusBadge(data.status)
                        }
                    }
                }

                // --- BENTO SECTION 2: TITLE & STATS ---
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
                            icon = Icons.Default.Schedule,
                            label = "Reported At",
                            value = data.createdAt.take(10) // Format tanggal simple
                        )
                        Spacer(Modifier.height(16.dp))
                        InfoRow(
                            icon = Icons.Default.LocationOn,
                            label = "Last Seen Location",
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

                // --- OWNER PROFILE CARD ---
                data.user?.let { user ->
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
                            // Avatar Placeholder/Image
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = data.user.profile?.avatar?: "https://i.pinimg.com/736x/8b/16/7a/8b167af653c2399dd93b952a48740620.jpg",
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(Modifier.width(16.dp))

                            Column(Modifier.weight(1f)) {
                                Text(
                                    text = data.user.profile?.username ?: "Anonymous User",
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
                }

                Spacer(Modifier.height(120.dp)) // Extra space for bottom button
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