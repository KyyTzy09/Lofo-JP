package com.fiky.lofo_app.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Announcement
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fiky.lofo_app.ui.theme.Primary
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)
@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        NavItem("home", Icons.Default.Home, "Home"),
        NavItem("announcement_user", Icons.Default.Announcement, "Announcement"),
        NavItem("scan", Icons.Default.QrCodeScanner, "Scan"),
        NavItem("announcement_create", Icons.Default.Add, "Add"),
        NavItem("profile", Icons.Default.Person, "Profile"),
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                clip = true
            }
            .shadow(elevation = 15.dp)
            .navigationBarsPadding(),
            color = Color(0xFF1A1A1A)
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier
                .padding(horizontal = 5.dp),
            windowInsets = WindowInsets(0.dp),
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { item ->
                val selected = currentRoute == item.route
                val isScan = item.route == "scan"
                val scale by animateFloatAsState(targetValue = if (selected) 1.2f else 1.0f)

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        if (isScan) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(
                                        color = if (selected) Primary else Primary.copy(alpha = 0.9f),
                                        shape = CircleShape
                                    )
                                    .scale(scale)
                                    .shadow(elevation = 8.dp, shape = CircleShape)) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label,
                                    modifier = Modifier.size(32.dp),
                                    tint = Color.White
                                )
                            }
                        } else {
                            val size by animateDpAsState(targetValue = if (selected) 28.dp else 24.dp)
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(size)
                            )
                        }
                    },
                    label = {
                        // Scan tidak butuh label karena iconnya sudah besar & jelas
                        if (selected && !isScan) {
                            Text(color = Color.White, text = item.label, fontWeight = FontWeight.Bold)
                        }
                    },
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = if (isScan) Color.Transparent else Primary.copy(alpha = 0.1f),
                        selectedIconColor = if (isScan) Color.White else Primary,
                        unselectedIconColor = Color.Gray
                    )
                )
            }
        }
    }
}