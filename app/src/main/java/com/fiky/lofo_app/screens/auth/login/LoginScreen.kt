package com.fiky.lofo_app.screens.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiky.lofo_app.R
import com.fiky.lofo_app.composables.CustomTextField
import com.fiky.lofo_app.composables.FieldLabel
import com.fiky.lofo_app.composables.ToastType
import com.fiky.lofo_app.utils.ToastHelper
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
    snackbarHostState: SnackbarHostState,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val state = viewModel.state
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary) // Bagian atas full warna primary
    ) {
        // --- HEADER SECTION (Logo & Title Aplikasi) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.2f) // Porsi ruang untuk area Logo
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Placeholder Logo Lingkaran (Bisa kamu ganti Image asset logo LoFo nanti)
            Image(
                painter = painterResource(id = R.drawable.icon_lofo),
                contentDescription = "LoFo Logo",
                modifier = Modifier
                    .size(160.dp) // Ukuran dikecilkan sedikit agar lebih proporsional
                    .clip(RoundedCornerShape(32.dp)), // Sesuaikan dengan bentuk icon kamu
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // --- FORM CONTAINER SECTION (White Card di bagian bawah) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2.8f) // Porsi ruang untuk form lebih besar
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(MaterialTheme.colorScheme.surface) // Form Container memakai warna surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 32.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Welcome Text
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Masuk ke akun yang terdaftar untuk melanjutkan",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
                )

                // Form Fields
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FieldLabel("Nomor Telepon")
                    CustomTextField(
                        value = state.phone,
                        onValueChange = { viewModel.onPhoneChange(it) },
                        placeholder = "+62 812...",
                        leadingIcon = Icons.Default.Phone
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

                Spacer(modifier = Modifier.height(28.dp))

                // CTA Button (Menggunakan warna primary solid agar serasi dengan top background)
                Button(
                    onClick = {
                        viewModel.login {
                            scope.launch {
                                ToastHelper.show(snackbarHostState, "Sukses", "Login Berhasil!", ToastType.SUCCESS)
                            }
                            onLoginSuccess()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !state.isLoading
                ) {
                    Text(
                        text = if (state.isLoading) "Memproses..." else "Login",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                // Error Message
                state.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Footer Links
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Belum memiliki akun? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Daftar",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier.clickable { onNavigateToRegister() }
                    )
                }
            }
        }
    }
}