package com.fiky.lofo_app.screens.auth.register

import com.fiky.lofo_app.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary) // Top background warna primary
    ) {
        // --- HEADER SECTION ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_lofo),
                contentDescription = "LoFo Logo",
                modifier = Modifier
                    .size(160.dp) // Ukuran dikecilkan sedikit agar lebih proporsional
                    .clip(RoundedCornerShape(32.dp)), // Sesuaikan dengan bentuk icon kamu
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "LoFo",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )
        }

        // --- FORM CONTAINER SECTION ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3.5f) // Form pendaftaran butuh scroll area lebih panjang
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 32.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Welcome Text
                Text(
                    text = "Buat Akun Baru",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Lengkapi data diri anda di bawah ini dengan benar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                // Form Fields
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
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

                Spacer(modifier = Modifier.height(16.dp))

                // Terms & Conditions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.agreeTerms,
                        onCheckedChange = { viewModel.onToggleTerms(it) },
                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = "Saya menyetujui Syarat dan Ketentuan",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // CTA Button
                Button(
                    onClick = {
                        viewModel.register {
                            scope.launch {
                                ToastHelper.show(snackbarHostState, "Sukses", "Akun Berhasil Dibuat!", ToastType.SUCCESS)
                            }
                            onRegisterSuccess()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Daftar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
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
                        text = "Sudah memiliki akun? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Masuk",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }
            }
        }
    }
}