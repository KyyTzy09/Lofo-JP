package com.fiky.lofo_app.screens.announcement.create

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAnnouncementScreen(
    onBack: () -> Unit,
    viewModel: CreateAnnouncementViewModel = viewModel(),
    snackbarHostState: SnackbarHostState,
    onSuccessfulCreate: (id: String) -> Unit = {}
) {
    val state = viewModel.state
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var tempDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var expandedItemSelector by remember { mutableStateOf(false) }
    val itemsList = listOf("MacBook Pro 16\"", "Leather Wallet", "Bose Headphones", "iPhone 13")


    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= System.currentTimeMillis()
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                            .format(java.util.Date(millis))
                        viewModel.onDateChange(date)
                        tempDate = date
                    }
                    showDatePicker = false
                    showTimePicker = true

                }) { Text("Pilih") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Batal") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = 10,
            initialMinute = 0,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val formattedTime = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                    viewModel.onDateChange("$tempDate $formattedTime") // Gabungkan Tanggal + Jam
                    showTimePicker = false
                }) { Text("Selesai") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Batal") }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Pilih Jam", modifier = Modifier.padding(bottom = 16.dp))
                    TimePicker(state = timePickerState)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Tambahkan Pengumuman",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White // primary color
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Header
            Text("Tambahkan Pengumuman", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF14002F))
            Text(
                "Mulai Pengumuman baru untuk mempermudah pencarian barang yang hilang",
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Form Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Field Judul
                    AnnouncementTextField(
                        label = "Judul Pengumuman",
                        value = state.title,
                        onValueChange = { viewModel.onTitleChange(it) },
                        placeholder = "Hp Samsung A53 Hilang"
                    )

                    // Field Deskripsi
                    AnnouncementTextField(
                        label = "Deskripsi (Optional)",
                        value = state.description,
                        onValueChange = { viewModel.onDescriptionChange(it) },
                        placeholder = "Deskripsikan pengumuman anda!",
                        singleLine = false,
                        minLines = 4
                    )

                    // Field Lokasi
                    AnnouncementTextField(
                        label = "Last Location",
                        value = state.lastLocation,
                        onValueChange = { viewModel.onLocationChange(it) },
                        placeholder = "Ruangan abc",
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray) }
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // --- SELECTOR BARANG ---
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Sambungkan Ke Barang (opsional)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF14002F)
                            )

                            ExposedDropdownMenuBox(
                                expanded = expandedItemSelector,
                                onExpandedChange = { expandedItemSelector = !expandedItemSelector }
                            ) {
                                OutlinedTextField(
                                    value = state.selectedItem ?: "",
                                    onValueChange = {},
                                    readOnly = true,
                                    placeholder = { Text("Pilih barang", color = Color.Gray) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedItemSelector) },
                                    modifier = Modifier.fillMaxWidth().menuAnchor(), // menuAnchor penting!
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedContainerColor = Color(0xFFF2F4F6),
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedBorderColor = Color(0xFF6D4EA2)
                                    )
                                )

                                ExposedDropdownMenu(
                                    expanded = expandedItemSelector,
                                    onDismissRequest = { expandedItemSelector = false },
                                    modifier = Modifier.background(Color.White)
                                ) {
                                    itemsList.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(item) },
                                            onClick = {
                                                viewModel.onItemSelected(item)
                                                expandedItemSelector = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // --- DATE PICKER FIELD ---
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Tanggal Hilang",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF14002F)
                            )

                            OutlinedTextField(
                                value = state.dateLost,
                                onValueChange = {},
                                readOnly = true, // Supaya user tidak ngetik manual
                                placeholder = { Text("Pilih tanggal", color = Color.Gray) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showDatePicker = true }, // Klik field buka kalender
                                enabled = false, // Supaya intercept click berjalan baik pada OutlinedTextField
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    IconButton(onClick = { showDatePicker = true }) {
                                        Icon(Icons.Default.DateRange, contentDescription = null, tint = Color(0xFF6D4EA2))
                                    }
                                },
                                // Custom warna karena enabled = false biasanya bikin teks jadi abu-abu pucat
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = Color.Black,
                                    disabledContainerColor = Color(0xFFF2F4F6),
                                    disabledBorderColor = Color.Transparent,
                                    disabledPlaceholderColor = Color.Gray,
                                    disabledTrailingIconColor = Color(0xFF6D4EA2)
                                )
                            )
                        }
                    }

                    // Submit Button
                    Button(
                        onClick = { viewModel.createAnnouncement(
                            onSuccess = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Pengumuman berhasil dibuat",
                                        withDismissAction = true
                                    )
                                    onSuccessfulCreate(it)
                                }
                            },
                            onError = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        it,
                                        withDismissAction = true
                                    )
                                    onBack()
                                }
                            }
                        )},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.linearGradient(
                                    listOf(Color(0xFF14002F),
                                    MaterialTheme.colorScheme.primary))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                 Text(
                                     if (state.isLoading) "Buat Pengumuman" else "Memproses..."
                                     , fontWeight = FontWeight.Bold
                                 )
                                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnnouncementTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF14002F))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = Color(0xFF6D4EA2),
                unfocusedIndicatorColor = Color.Gray,
            ),
            leadingIcon = leadingIcon,
            singleLine = singleLine,
            minLines = minLines
        )
    }
}