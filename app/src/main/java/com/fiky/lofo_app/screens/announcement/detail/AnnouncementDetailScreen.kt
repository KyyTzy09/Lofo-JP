package com.fiky.lofo_app.screens.announcement.detail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementDetailScreen(
    announcementId: String,
    viewModel: AnnouncementDetailViewModel,
    onBack: () -> Unit
) {
    val state = viewModel.state
    val scrollState = rememberScrollState()

    LaunchedEffect(announcementId) {
        viewModel.getDetail(announcementId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            // Action Button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Button(
                    onClick = { /* Handle Found Item */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D4EA2))
                ) {
                    Text("I FOUND THIS ITEM", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF6D4EA2))
            }
        } else if (state.announcement != null) {
            val data = state.announcement

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // --- BENTO SECTION 1: IMAGE ---
                // Hanya tampil jika ada itemId (mengikuti logika "koneksi gambar")
                // Jika API kamu punya field image_url sendiri di Announcement, ganti ke situ.
                if (data.itemId != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(Color.LightGray)
                    ) {
                        AsyncImage(
                            model = "https://your-api-url.com/images/${data.itemId}.jpg", // Contoh URL
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Status Badge
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Badge(containerColor = Color(0xFF14002F), contentColor = Color.White) {
                                Text(data.status.name, modifier = Modifier.padding(4.dp))
                            }
                        }
                    }
                }

                // --- BENTO SECTION 2: FAST INFO ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FB)),
                    border = BorderStroke(1.dp, Color(0xFF310065).copy(alpha = 0.05f))
                ) {
                    Column(Modifier.padding(24.dp)) {
                        Text(
                            text = "ANNOUNCEMENT DETAIL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF6D4EA2),
                            letterSpacing = 1.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = data.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF14002F)
                        )
                        Spacer(Modifier.height(16.dp))

                        InfoRow(icon = Icons.Default.Schedule, label = "Lost at", value = data.createdAt)
                        Spacer(Modifier.height(12.dp))
                        InfoRow(icon = Icons.Default.LocationOn, label = "Location", value = data.location)
                    }
                }

                // --- DESCRIPTION ---
                Column {
                    Text(
                        "DESCRIPTION",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = Color(0xFF14002F)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = data.description,
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        lineHeight = 24.sp
                    )
                }

                // --- OWNER PROFILE ---
                data.user?.let { user ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF310065))
                    ) {
                        Row(
                            modifier = Modifier.padding(24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(alpha = 0.2f))
                            ) {
                                // Ganti dengan user.profile_picture jika ada
                                Text("👤", Modifier.align(Alignment.Center))
                            }
                            Spacer(Modifier.width(16.dp))
                            Column(Modifier.weight(1f)) {
                                Text(user?.profile?.username ?: "Tidak diketahui", color = Color.White, fontWeight = FontWeight.Bold)
                                Text("Owner Profile", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                            }
                            IconButton(onClick = { /* Chat logic */ }) {
                                Icon(Icons.Default.ChatBubble, contentDescription = "")
                            }
                        }
                    }
                }

                Spacer(Modifier.height(100.dp)) // Padding bawah agar tidak tertutup button
            }
        }
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF6D4EA2).copy(alpha = 0.1f),
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color(0xFF6D4EA2),
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}