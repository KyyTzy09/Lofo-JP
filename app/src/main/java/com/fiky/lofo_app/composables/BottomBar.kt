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
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.fiky.lofo_app.ui.theme.Primary

data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        NavItem("home", Icons.Default.Home, "Home"),
        NavItem("announcement_create", Icons.Default.Add, "Add"),
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
                label = {
                    if (currentRoute != item.route) {
                        Text(item.label);
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Primary, // background item yang selected
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Primary,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}
