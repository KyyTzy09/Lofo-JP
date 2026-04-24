package com.fiky.lofo_app.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController

data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        NavItem("home", Icons.Default.Home, "Home"),
        NavItem("item/create", Icons.Default.Add, "Add"),
        NavItem("scan", Icons.Default.QrCodeScanner, "Scan"),
        NavItem("profile", Icons.Default.Person, "Profile"),
    )

    NavigationBar {
        val currentRoute = navController.currentBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo("home")
                        launchSingleTop = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}