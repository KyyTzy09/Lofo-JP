package com.fiky.lofo_app

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.fiky.lofo_app.screens.auth.login.LoginScreen
import com.fiky.lofo_app.screens.auth.login.LoginViewModel
import com.fiky.lofo_app.screens.auth.register.RegisterScreen
import com.fiky.lofo_app.screens.auth.register.RegisterViewModel
import com.fiky.lofo_app.screens.home.HomeScreen
import com.fiky.lofo_app.screens.home.HomeViewModel
import com.fiky.lofo_app.screens.item.CreateItemScreen
import com.fiky.lofo_app.screens.onboarding.OnboardingScreen
import com.fiky.lofo_app.screens.scan.ScanController
import com.fiky.lofo_app.screens.scan.ScanScreen

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
            OnboardingScreen(
                navController
            )
        }

        composable("scan") {
            val viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
            MainLayout(navController) {
                ScanScreen(
                onNavigateToDetail = {
                    navController.navigate("item/detail/$it")
                },
                onBack = { navController.popBackStack() }
                )
            }
        }


        navigation(startDestination = "home", route = "main") {
            composable("home") {
                val viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                MainLayout(navController) {
                    HomeScreen(
                        navController,
                        viewModel
                    )
                }
            }
        }

        navigation(startDestination = "item/create", route="item") {
            composable("item/detail/{itemId}") {
                val itemId = it.arguments?.getString("itemId")
                Text("Detail")
            }
            composable("item/create") {
                CreateItemScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }

        navigation(startDestination = "login", route = "auth") {
            composable("login") {
                val viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                LoginScreen(
                navController = navController,viewModel = viewModel
            ) }
            composable("register") {
                val viewModel: RegisterViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                RegisterScreen(
                navController = navController,
                viewModel = viewModel
            ) }
        }
    }
}