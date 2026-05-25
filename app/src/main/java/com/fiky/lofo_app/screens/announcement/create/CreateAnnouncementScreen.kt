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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAnnouncementScreen(
    onBack: () -> Unit,
    viewModel: CreateAnnouncementViewModel = viewModel(),
    snackbarHostState: SnackbarHostState,
    onSuccessfulCreate: (id: String) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.getUserItems()
    }

    val state = viewModel.state
    val items = state.items

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var tempDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var expandedItemSelector by remember { mutableStateOf(false) }

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
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface, // Warna latar belakang
                    titleContentColor = MaterialTheme.colorScheme.primary, // Warna teks judul (Pilih Tanggal)
                    headlineContentColor = MaterialTheme.colorScheme.primary, // Warna teks tanggal yang terpilih di atas
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary, // Warna buletan tanggal terpilih
                    selectedDayContentColor = MaterialTheme.colorScheme.onPrimary, // Warna angka tanggal terpilih
                    todayContentColor = MaterialTheme.colorScheme.primary, // Warna angka hari ini
                    todayDateBorderColor = MaterialTheme.colorScheme.primary // Warna lingkaran hari ini
                )
            )
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
            containerColor = MaterialTheme.colorScheme.surface,
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
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            // Lingkaran jam (pake surface yang agak terang dikit biar kontras sama background)
                            clockDialColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            // Angka di dalam lingkaran yang tidak dipilih
                            clockDialUnselectedContentColor = MaterialTheme.colorScheme.primary,
                            // Angka di dalam lingkaran saat kena jarum jam
                            clockDialSelectedContentColor = Color.White,
                            // Warna jarum jam (pake ungu utama lo)
                            selectorColor = MaterialTheme.colorScheme.primary,
                            // Warna kotak jam/menit (Input)
                            timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            // Warna angka di dalam kotak yang dipilih (Harus kontras!)
                            timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            // Warna kotak jam/menit yang lagi GA dipilih
                            timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            // Warna angka di kotak yang lagi GA dipilih
                            timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
//
//            // Header Section - Lebih Simple & Modern
//            Text(
//                "Buat Pengumuman",
//                style = MaterialTheme.typography.headlineMedium,
//                fontWeight = FontWeight.ExtraBold,
//                color = MaterialTheme.colorScheme.onBackground
//            )
//            Text(
//                "Lengkapi detail barang untuk mempercepat proses penemuan.",
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//
//            Spacer(modifier = Modifier.height(32.dp))

            // Main Form Container
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface, // Warna gelap yang sedikit lebih terang
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(MaterialTheme.colorScheme.surfaceContainer, shape = RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(Icons.Default.Announcement, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Buat Pengumuman",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // --- INPUT FIELD: JUDUL ---
                    AnnouncementTextField(
                        label = "Judul",
                        value = state.title,
                        onValueChange = { viewModel.onTitleChange(it) },
                        placeholder = "Contoh: Kehilangan Laptop LOQ"
                    )

                    // --- INPUT FIELD: LOKASI ---
                    AnnouncementTextField(
                        label = "Lokasi Terakhir",
                        value = state.lastLocation,
                        onValueChange = { viewModel.onLocationChange(it) },
                        placeholder = "Gedung Lab, Kantin, dsb.",
                        leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = MaterialTheme.colorScheme.primary) }
                    )

                    // --- INPUT FIELD: ITEM (Opsional) ---
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Hubungkan Ke Barang (opsional)",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        ExposedDropdownMenuBox(
                            expanded = expandedItemSelector,
                            onExpandedChange = { expandedItemSelector = !expandedItemSelector },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        {
                            OutlinedTextField(
                                value = state.selectedItemName ?: "",
                                onValueChange = {},
                                readOnly = true,
                                placeholder = {
                                    Text(
                                        "Pilih barang",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedItemSelector)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                    focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )

                            // Gaya Dropdown Menu (Popup)
                            MaterialTheme(
                                colorScheme = MaterialTheme.colorScheme.copy(surface = MaterialTheme.colorScheme.surfaceContainerHighest)
                            ) {
                                ExposedDropdownMenu(
                                    expanded = expandedItemSelector,
                                    onDismissRequest = { expandedItemSelector = false },
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.surface)
                                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    items?.forEach { item ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = item?.itemName?: "Nama Barang",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onPrimary
                                                )
                                            },
                                            onClick = {
                                                viewModel.onItemSelected(item.itemId, item.itemName)
                                                expandedItemSelector = false
                                            },
                                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    // --- INPUT FIELD: TANGGAL (Modern Style) ---
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Kapan Terjadi?", fontWeight = FontWeight.Bold, color =  MaterialTheme.colorScheme.onSurface)
                        Surface(
                            onClick = { showDatePicker = true },
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CalendarMonth, null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    text = if(state.dateLost.isEmpty()) "Pilih Tanggal & Waktu" else state.dateLost,
                                    color = if(state.dateLost.isEmpty()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }

                    // --- INPUT FIELD: DESKRIPSI ---
                    AnnouncementTextField(
                        label = "Catatan Tambahan",
                        value = state.description,
                        onValueChange = { viewModel.onDescriptionChange(it) },
                        placeholder = "Ciri fisik, warna casing, dsb.",
                        singleLine = false,
                        minLines = 3
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- SUBMIT BUTTON ---
                    Button(
                        onClick = {
                            viewModel.createAnnouncement(
                                onSuccess = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Berhasil membuat pengumuman")
                                        onBack()
                                        onSuccessfulCreate(it)
                                    }
                                    onSuccessfulCreate(it)
                                },
                                onError = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(it)
                                    }
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Publikasikan Sekarang", fontWeight = FontWeight.Bold)
                                Spacer(Modifier.width(8.dp))
                                Icon(Icons.Default.Send, null, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
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
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            leadingIcon = leadingIcon,
            singleLine = singleLine,
            minLines = minLines,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}