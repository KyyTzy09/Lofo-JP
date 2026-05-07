package com.fiky.lofo_app.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fiky.lofo_app.data.models.AnnouncementStatus
import com.fiky.lofo_app.screens.announcement.AnnouncementCard
import com.fiky.lofo_app.screens.profile.global.GlobalProfileViewModel
import androidx.compose.runtime.getValue

@Composable
fun HomeScreen(
    onNavigateAnnouncementDetail : (id: String) -> Unit,
    onNavigateToAnnouncementCreate: () -> Unit,
    profileViewModel: GlobalProfileViewModel = viewModel(),
    viewModel: HomeViewModel = viewModel()
) {
    val state = viewModel.state
    val userState by profileViewModel.userState.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadUserProfile()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { HomeTopAppBar(userState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // 1. Search Bar
            item {
                SearchBar(
                    query = state.searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChange(it) }
                )
            }

            // 2. Hero Section (RecoverEase Banner)
            item { HeroSection(
                pendingCount = state.pendingAnnouncements.size,
                onNavigateToAnnouncementCreate
            ) }

            // 3. Pending Announcements
            item {
                SectionHeader("Pengumuman Berlangsung", false)
            }

            if (state.pendingAnnouncements.isEmpty()) {
                item { EmptyStateMessage("Tidak ada pengumuman yang sedang berlangsung") }
            } else {
                items(state.pendingAnnouncements) { announcement ->
                    AnnouncementCard(announcement, onNavigateAnnouncementDetail)
                }
            }

            // 4. Closed Announcements
            item {
                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader("Pengumuman Selesai", false)
            }

            if (state.closedAnnouncements.isEmpty()) {
                item { EmptyStateMessage("Tidak ada pengumuman yang telah selesai") }
            } else {
                items(state.closedAnnouncements) { announcement ->
                    AnnouncementCard(announcement, onNavigateAnnouncementDetail)
                }
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(16.dp)),
        placeholder = { Text("Search for lost items...", color = MaterialTheme.colorScheme.primary) },
        leadingIcon = { Icon(Icons.Default.Search, null) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            unfocusedTextColor = MaterialTheme.colorScheme.primary
        )
    )
}


@Composable
fun StatusBadge(status: AnnouncementStatus) {
    val bgColor = if (status == AnnouncementStatus.PENDING)
        MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.tertiary

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(
            24.dp
        ),
    ) {
        Text(
            text = status.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}

@Composable
fun SectionHeader(title: String, showViewAll: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        if (showViewAll) {
            Text(
                "View All",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun IconText(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, style = MaterialTheme.typography.bodySmall, color = Color.White)
    }
}

@Composable
fun EmptyStateMessage(msg: String) {
    Text(
        msg,
        modifier = Modifier.padding(24.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyMedium
    )
}