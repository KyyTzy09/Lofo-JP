package com.fiky.lofo_app.screens.profile.update

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fiky.lofo_app.composables.ToastType
import com.fiky.lofo_app.screens.profile.global.GlobalProfileViewModel
import com.fiky.lofo_app.utils.ToastHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileScreen(
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    globalProfileViewModel: GlobalProfileViewModel,
    viewModel: UpdateProfileViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val state = viewModel.state
    val userProfile by globalProfileViewModel.userState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("Update Profile", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    Surface(
                        modifier = Modifier.padding(end = 16.dp).size(40.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        AsyncImage(
                            model = userProfile.profilePicture,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Decorations
            Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.align(Alignment.TopEnd).size(300.dp).offset(x = 100.dp, y = (-50).dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), RoundedCornerShape(16.dp)).clip(RoundedCornerShape(16.dp)))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Avatar Display (Read Only)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier
                            .size(120.dp)
                            .border(2.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(32.dp)),
                        shape = RoundedCornerShape(32.dp),
                        color = MaterialTheme.colorScheme.surfaceContainer
                    ) {
                        AsyncImage(
                            model = userProfile.profilePicture,
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Profil Photo",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Form Fields
                UpdateInputFields(
                    label = "Nama",
                    value = state.name,
                    onValueChange = viewModel::onNameChange,
                    placeholder = "Masukan namamu",
                    icon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(20.dp))

                UpdateInputFields(
                    label = "Deskripsi",
                    value = state.description,
                    onValueChange = viewModel::onDescriptionChange,
                    placeholder = "Berikan deskripsi tentang dirimu",
                    icon = Icons.Default.Info,
                    isTextArea = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                UpdateInputFields(
                    label = "Alamat",
                    value = state.address,
                    onValueChange = viewModel::onAddressChange,
                    placeholder = "Alamat Kamu",
                    icon = Icons.Default.LocationOn
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Security Tip Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Tips", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
                            Text(
                                "Perbarui Info anda supaya identitas anda mudah dikenali",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { viewModel.updateProfile(
                        onSuccess = {
                            scope.launch {
                                ToastHelper.show(snackbarHostState, "Sukses Memperbarui Profile", "Profile Telah Diperbarui!", ToastType.SUCCESS)
                                onBack()
                            } },
                        onError = {
                            scope.launch {
                                ToastHelper.show(snackbarHostState, "Gagal Memperbarui Profile", it, ToastType.ERROR)
                            }
                        }
                    )},
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (!state.isLoading) {
                            Text(
                                "Simpan",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        } else {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }

                Text(
                    "Dengan menambahkan barang ini, Anda menyetujui Syarat Ketentuan dan Kebijakan Privasi",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                )
            }
        }
    }
}

@Composable
fun UpdateInputFields(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isTextArea: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            leadingIcon = { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            shape = RoundedCornerShape(16.dp),
            minLines = if (isTextArea) 4 else 1,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
              )
        )
    }
}