package com.fiky.lofo_app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.fiky.lofo_app.screens.announcement.user.UserAnnouncementScreen
import com.fiky.lofo_app.screens.auth.login.LoginScreen
import com.fiky.lofo_app.screens.auth.login.LoginViewModel
import com.fiky.lofo_app.screens.auth.register.RegisterScreen
import com.fiky.lofo_app.screens.auth.register.RegisterViewModel
import com.fiky.lofo_app.screens.home.HomeScreen
import com.fiky.lofo_app.screens.home.HomeViewModel
import com.fiky.lofo_app.screens.item.create.CreateItemScreen
import com.fiky.lofo_app.screens.item.detail.ItemDetailScreen
import com.fiky.lofo_app.screens.item.update.UpdateItemScreen
import com.fiky.lofo_app.screens.item.user.UserItemScreen
import com.fiky.lofo_app.screens.onboarding.OnboardingScreen
import com.fiky.lofo_app.screens.profile.detail.ProfileScreen
import com.fiky.lofo_app.screens.profile.global.GlobalProfileViewModel
import com.fiky.lofo_app.screens.profile.update.UpdateProfileScreen
import com.fiky.lofo_app.screens.scan.ScanScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    authenticated: Boolean,
) {
    val globalProfileViewModel: GlobalProfileViewModel = viewModel()
    val navController = rememberNavController()
    val startDest = if (authenticated) "home" else "onboarding"

    MainLayout(
        navController
    ) {
    NavHost(
        navController = navController,
        startDestination = startDest,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400) // durasi 400ms
            ) + fadeIn(animationSpec = tween(400))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            ) + fadeOut(animationSpec = tween(400))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            ) + fadeOut(animationSpec = tween(400))
        }
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
            HomeScreen(
                onNavigateAnnouncementDetail = {
                    navController.navigate("announcement_detail/$it")
                },
                onNavigateToAnnouncementCreate = {
                    navController.navigate("announcement_create")
                },
                globalProfileViewModel,
                viewModel

            )
        }

        // Profile
        composable("profile") {
            ProfileScreen(
                navController,
                snackbarHostState,
                globalProfileViewModel= globalProfileViewModel,
                viewModel = viewModel()
            )
        }

        composable("profile_update") {
            UpdateProfileScreen(
                onBack = { navController.popBackStack() },
                snackbarHostState,
                globalProfileViewModel
            )
        }

        composable("scan") {
            ScanScreen(
                onNavigateToDetail = { itemId ->
                    navController.navigate("item_detail/$itemId") },
                onBack = { navController.popBackStack() }
            )
        }

        // --- ITEM FEATURE ---
        composable("item_user") {
                UserItemScreen(
                    viewModel = viewModel(),
                    onNavigateToDetail = { itemId ->
                        navController.navigate("item_detail/$itemId")
                    },
                    onAddItem = { navController.navigate("item_create") },
                    onUpdateItem = { itemId -> navController.navigate("item_update/$itemId") }
                )
        }

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

        composable("item_update/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
                ?: return@composable
            UpdateItemScreen(
                itemId,
                onBack = { navController.popBackStack() },
                snackbarHostState = snackbarHostState
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

        composable("announcement_user") {
            UserAnnouncementScreen(
                onBack = { navController.popBackStack() },
                onNavigateToDetail = {
                    navController.navigate("announcement_detail/$it")
                },
                onNavigateToCreate = {
                    navController.navigate("announcement_create")
                }
            )
        }

        composable("announcement_detail/{announcementId}") { backStackEntry ->
            val announcementId = backStackEntry.arguments?.getString("announcementId")
                ?: return@composable
            AnnouncementDetailScreen(
                announcementId,
                onBack = { navController.popBackStack() },
                viewModel = viewModel(),
                globalProfileViewModel
            )
        }
    }}
}