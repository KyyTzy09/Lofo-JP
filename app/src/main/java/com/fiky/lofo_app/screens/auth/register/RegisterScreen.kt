package com.fiky.lofo_app.screens.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fiky.lofo_app.composables.CustomTextField
import com.fiky.lofo_app.composables.FieldLabel
import com.fiky.lofo_app.composables.ToastType
import com.fiky.lofo_app.utils.ToastHelper
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel,
    snackbarHostState: SnackbarHostState,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin:() -> Unit
) {
    val scope = rememberCoroutineScope()
    val state = viewModel.state
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Background Decoration (Blobs)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-100).dp, y = (-100).dp)
                .size(300.dp)
                .blur(100.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(400.dp)
                .blur(120.dp)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            // Header Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Buat Akun Baru",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 32.sp
                    )
                )
                Text(
                    text = "Buat akun baru anda untuk mulai menggunakan aplikasi",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Form Fields
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                FieldLabel("Username")
                CustomTextField(
                    value = state.username,
                    onValueChange = { viewModel.onUsernameChange(it) },
                    placeholder = "john doe",
                    leadingIcon = Icons.Default.Person
                )

                FieldLabel("Nomor Telepon")
                CustomTextField(
                    value = state.phoneNumber,
                    onValueChange = { viewModel.onPhoneChange(it) },
                    placeholder = "+62 812...",
                    leadingIcon = Icons.Default.Phone
                )

                FieldLabel("Alamat")
                CustomTextField(
                    value = state.address,
                    onValueChange = { viewModel.onAddressChange(it) },
                    placeholder = "Jl. Sudirman No. 123",
                    leadingIcon = Icons.Default.LocationOn
                )

                FieldLabel("Password")
                CustomTextField(
                    value = state.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    placeholder = "••••••••",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Terms & Conditions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(
                    checked = state.agreeTerms,
                    onCheckedChange = { viewModel.onToggleTerms(it) },
                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                )
                Text(
                    text = "Saya telah membaca dan menyetujui Syarat dan Ketentuan",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // CTA Button with Gradient
            val gradient = Brush.linearGradient(
                colors = listOf(MaterialTheme.colorScheme.primary, Color(0xFF4A148C))
            )

            Button(
                onClick = { viewModel.register {
                    scope.launch {
                        ToastHelper.show(snackbarHostState, "Sukses", "Akun Berhasil Dibuat!", ToastType.SUCCESS)
                    }
                    onRegisterSuccess()
                } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(gradient),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(16.dp),
                enabled = !state.isLoading
            ) {
                Text(
                    text = if (state.isLoading) "Memproses..." else "Daftar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Error Message
            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer Links
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Sudah memiliki akun?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Masuk",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}

