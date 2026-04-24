package com.fiky.lofo_app

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fiky.lofo_app.screens.onboarding.OnboardingScreen

@Composable()
fun AppNavigation (
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {
        composable("onboarding") {
            OnboardingScreen()
        }
        composable("home") {
            Text("Home Screen")
        }
    }
}