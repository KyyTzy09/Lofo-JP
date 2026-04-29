package com.fiky.lofo_app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fiky.lofo_app.screens.auth.AuthState
import com.fiky.lofo_app.screens.auth.AuthViewModel

@Composable
fun AppRoot(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    viewModel: AuthViewModel = viewModel()) {
    val state by viewModel.authState.collectAsState()
    when (state) {
        is AuthState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is AuthState.Authenticated -> {
            AppNavigation(
                modifier,
                snackbarHostState,
                authenticated = true
            )
        }

        is AuthState.Unauthenticated -> {
            AppNavigation(
                modifier,
                snackbarHostState,
                authenticated = false
            )
        }
    }
}