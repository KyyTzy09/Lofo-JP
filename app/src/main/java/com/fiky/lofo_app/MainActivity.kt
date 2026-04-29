package com.fiky.lofo_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fiky.lofo_app.composables.LofoToast
import com.fiky.lofo_app.composables.ToastType
import com.fiky.lofo_app.screens.auth.AuthViewModel
import com.fiky.lofo_app.screens.onboarding.OnboardingScreen
import com.fiky.lofo_app.ui.theme.LoFo_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            LoFo_AppTheme {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) { snackbarData ->
                            val parts = snackbarData.visuals.message.split("|")

                            val title = parts.getOrNull(0) ?: ""
                            val desc = parts.getOrNull(1) ?: ""
                            val type = try {
                                ToastType.valueOf(parts.getOrNull(2) ?: "INFO")
                            } catch (e: Exception) {
                                ToastType.INFO
                            }

                            LofoToast(
                                title = title,
                                description = desc,
                                type = type
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppRoot(
                        modifier = Modifier.padding(innerPadding),
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}
