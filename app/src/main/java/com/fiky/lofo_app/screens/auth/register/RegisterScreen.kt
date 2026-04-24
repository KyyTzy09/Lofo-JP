package com.fiky.lofo_app.screens.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fiky.lofo_app.composables.CustomTextField

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: RegisterViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state = viewModel.state
    var agree by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        // Title
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter your details to begin your journey.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // USERNAME
        CustomTextField(
            value = state.username,
            onValueChange = { viewModel.onUsernameChange(it) },
            placeholder = "Username"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // PHONE
        CustomTextField(
            value = state.phone,
            onValueChange = { viewModel.onPhoneChange(it) },
            placeholder = "Nomor HP"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // PASSWORD
        CustomTextField(
            value = state.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            placeholder = "Password",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TERMS
        Row(
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(
                checked = agree,
                onCheckedChange = { agree = it }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Saya setuju dengan Terms & Privacy Policy",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // BUTTON
        Button(
            onClick = {
                viewModel.register {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            },
            enabled = agree && !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(if (state.isLoading) "Loading..." else "Sign Up",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
