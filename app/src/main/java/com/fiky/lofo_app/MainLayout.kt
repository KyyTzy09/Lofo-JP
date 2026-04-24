package com.fiky.lofo_app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        "onboarding"
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}