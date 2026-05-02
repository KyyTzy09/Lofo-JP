package com.fiky.lofo_app

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fiky.lofo_app.layouts.MainLayout
import com.fiky.lofo_app.screens.announcement.create.CreateAnnouncementScreen
import com.fiky.lofo_app.screens.announcement.detail.AnnouncementDetailScreen
import com.fiky.lofo_app.screens.auth.login.LoginScreen
import com.fiky.lofo_app.screens.auth.login.LoginViewModel
import com.fiky.lofo_app.screens.auth.register.RegisterScreen
import com.fiky.lofo_app.screens.auth.register.RegisterViewModel
import com.fiky.lofo_app.screens.home.HomeScreen
import com.fiky.lofo_app.screens.home.HomeViewModel
import com.fiky.lofo_app.screens.item.create.CreateItemScreen
import com.fiky.lofo_app.screens.item.detail.ItemDetailScreen
import com.fiky.lofo_app.screens.onboarding.OnboardingScreen
import com.fiky.lofo_app.screens.profile.ProfileScreen
import com.fiky.lofo_app.screens.profile.ProfileViewModel
import com.fiky.lofo_app.screens.scan.ScanScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    authenticated: Boolean
) {
    val navController = rememberNavController()
    val startDest = if (authenticated) "home" else "onboarding"

    NavHost(
        navController = navController,
        startDestination = startDest,
        modifier = modifier
    ) {
        // --- ONBOARDING & AUTH ---
        composable("onboarding") {
            OnboardingScreen(navController)
        }

        composable("login") {
            val viewModel: LoginViewModel = viewModel()
            LoginScreen(
                viewModel = viewModel,
                snackbarHostState = snackbarHostState,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            val viewModel: RegisterViewModel = viewModel()
            RegisterScreen(
                viewModel = viewModel,
                snackbarHostState = snackbarHostState,
                onRegisterSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.navigate("login") }
            )
        }

        // --- MAIN APP CONTENT ---
        composable("home") {
            val viewModel: HomeViewModel = viewModel()
            MainLayout(navController) {
                HomeScreen(navController, viewModel)
            }
        }

        composable("profile") {
            val viewModel: ProfileViewModel = viewModel()
            MainLayout(navController) {
                ProfileScreen(navController, viewModel)
            }
        }

        composable("scan") {
            MainLayout(navController) {
                ScanScreen(
                    onNavigateToDetail = { itemId ->
                        navController.navigate("item_detail/$itemId")
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        // --- ITEM FEATURE ---
        composable("item_create") {
            CreateItemScreen(
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate("item_detail/$it")
                },
                snackbarHostState = snackbarHostState
            )
        }

        composable("item_detail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
                ?: return@composable
            ItemDetailScreen(
                itemId = itemId,
                onBack = { navController.popBackStack() }
            )
        }

        // --- ANNOUNCEMENT FEATURE ---
        composable("announcement_create") {
            CreateAnnouncementScreen(
                onBack = { navController.popBackStack() },
                snackbarHostState=snackbarHostState,
                onSuccessfulCreate = {
                    navController.navigate("item_detail/$it") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("announcement_detail/{announcementId}") { backStackEntry ->
            val announcementId = backStackEntry.arguments?.getString("announcementId")
                ?: return@composable
            AnnouncementDetailScreen(
                announcementId,
                onBack = { navController.popBackStack() },
                viewModel = viewModel()
            )
        }
    }
}