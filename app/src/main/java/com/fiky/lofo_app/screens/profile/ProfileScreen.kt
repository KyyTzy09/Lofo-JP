package com.fiky.lofo_app.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val state = viewModel.state
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(onClick = { /* Open Menu */ }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color(0xFF310065)
                            )
                        }
                        Text(
                            "The Curated Recovery",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF310065),
                                letterSpacing = (-0.5).sp
                            )
                        )
                    }
                },
                actions = {
                    AsyncImage(
                        model = state.profileImage,
                        contentDescription = "User Profile",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color(0xFFEDDCFF), CircleShape),
                        contentScale = ContentScale.Crop
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF8F9FB).copy(alpha = 0.8f)
                )
            )
        },
        containerColor = Color(0xFFF8F9FB)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // USER INFO SECTION
            UserInfoSection(state)

            Spacer(modifier = Modifier.height(40.dp))

            // REPORTED ITEMS SECTION
            ReportedItemsSection(state)

            Spacer(modifier = Modifier.height(40.dp))

            // ACCOUNT SETTINGS SECTION
            AccountSettingsSection { viewModel.logout() }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun UserInfoSection(state: ProfileState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFFF2F4F6))
            .padding(24.dp)
    ) {
        // Decorative blob
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-40).dp)
                .size(150.dp)
                .blur(40.dp)
                .background(Color(0xFF310065).copy(alpha = 0.05f), CircleShape)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Box {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(
                                listOf(Color(0xFF310065), Color(0xFFC5A3FF), Color(0xFF310065))
                            )
                        )
                        .padding(3.dp)
                ) {
                    AsyncImage(
                        model = state.profileImage,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(4.dp, Color.White, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF310065))
                        .clickable { /* Edit profile */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Column {
                Text(
                    state.name,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF310065)
                    )
                )
                Text(
                    state.email,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF4A4452),
                        fontWeight = FontWeight.Medium
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Tag(text = state.memberStatus, containerColor = Color(0xFF310065), contentColor = Color.White)
                    Tag(text = "${state.itemsRecovered} Items Recovered", containerColor = Color(0xFFC5A3FF), contentColor = Color(0xFF533487))
                }
            }
        }
    }
}

@Composable
fun Tag(text: String, containerColor: Color, contentColor: Color) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(containerColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 0.5.sp
            ),
            color = contentColor
        )
    }
}

@Composable
fun ReportedItemsSection(state: ProfileState) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    "My Reported Items",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF310065)
                    )
                )
                Text(
                    "A curated timeline of your active reports",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF4A4452)
                    )
                )
            }
            Text(
                "View Archive",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF310065)
                ),
                modifier = Modifier.clickable { /* View Archive */ }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Large Card
            state.reportedItems.find { it.type == ReportedItemType.LARGE }?.let { item ->
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .height(240.dp)
                        .clip(RoundedCornerShape(32.dp))
                ) {
                    AsyncImage(
                        model = item.image,
                        contentDescription = item.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        Tag(
                            text = "Reported ${item.timeAgo}",
                            containerColor = Color.White.copy(alpha = 0.2f),
                            contentColor = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            item.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            "Last seen near ${item.location}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(20.dp)
                            .size(44.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF4A148C)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFB889FF))
                    }
                }
            }

            // Right column for small cards
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // In Progress Card
                state.reportedItems.find { it.type == ReportedItemType.SMALL }?.let { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(32.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE1E2E4).copy(alpha = 0.5f), RoundedCornerShape(32.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(Icons.Default.Watch, contentDescription = null, tint = Color(0xFF310065), modifier = Modifier.size(28.dp))
                            Text(
                                item.status?.uppercase() ?: "",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 8.sp,
                                    letterSpacing = (-0.5).sp
                                ),
                                color = Color(0xFF310065)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            item.name,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, lineHeight = 18.sp),
                            maxLines = 2
                        )
                        Text(
                            "Found record in ${item.location}",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF4A4452), fontSize = 9.sp),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        LinearProgressIndicator(
                            progress = { item.progress ?: 0f },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                            color = Color(0xFF310065),
                            trackColor = Color(0xFFE1E2E4),
                            strokeCap = StrokeCap.Round
                        )
                    }
                }

                // Add New Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(32.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF310065), Color(0xFF4A148C))
                            )
                        )
                        .clickable { /* Report New */ }
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                        Text(
                            "Report New",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = Color.White)
                        )
                        Text(
                            "Lost or found?",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AccountSettingsSection(onLogout: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            "Account Settings",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF310065)
            ),
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFFF2F4F6))
        ) {
            SettingItem(
                icon = Icons.Default.ManageAccounts,
                title = "Edit Profile Information",
                onClick = {}
            )
            SettingItem(
                icon = Icons.Default.NotificationsActive,
                title = "Notification Preferences",
                badge = "Active",
                onClick = {}
            )
            SettingItem(
                icon = Icons.Default.Security,
                title = "Security & Privacy",
                onClick = {}
            )
            SettingItem(
                icon = Icons.AutoMirrored.Filled.Logout,
                title = "Logout Session",
                isError = true,
                onClick = onLogout
            )
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    badge: String? = null,
    isError: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isError) Color(0xFFFFDAD6).copy(alpha = 0.5f) else Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isError) Color(0xFFBA1A1A) else Color(0xFF310065),
                modifier = Modifier.size(24.dp)
            )
        }
        
        Text(
            title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = if (isError) Color(0xFFBA1A1A) else Color(0xFF191C1E),
            modifier = Modifier.weight(1f)
        )

        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFEDDCFF))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    badge,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF310065))
                )
            }
        }

        Icon(
            if (isError) Icons.AutoMirrored.Filled.Logout else Icons.Default.ChevronRight,
            contentDescription = null,
            tint = if (isError) Color(0xFFBA1A1A).copy(alpha = 0.4f) else Color(0xFF7C7483)
        )
    }
}
