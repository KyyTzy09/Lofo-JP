package com.fiky.lofo_app.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fiky.lofo_app.composables.BottomNavBar

@Composable
fun MainLayout(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    val showBottomBar = currentRoute !in listOf(
        "login",
        "register",
        "onboarding",
        "item_create",
        "item_detail/{itemId}",
        "announcement_detail/{announcementId}",
        "profile_update"
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController)
            }
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (showBottomBar) 0.dp else innerPadding.calculateBottomPadding())
        ) {
            content()
        }
    }
}