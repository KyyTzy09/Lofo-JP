package com.fiky.lofo_app.screens.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp
import com.fiky.lofo_app.composables.CustomTextField

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state = viewModel.state
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        // Title
        Text(
            text = "Login",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Masuk ke LoFo untuk melanjutkan",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))
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

        // BUTTON
        Button (
            onClick = {
                viewModel.login {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            },
            enabled = !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(if (state.isLoading) "Loading..." else "Sign Iffn",
                style = MaterialTheme.typography.labelLarge
            )
        }

        state.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Belum Memiliki Akun?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                modifier = Modifier
                    .clickable(
                        onClick = { navController.navigate("register") }
                    ),
                text = "Register",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
