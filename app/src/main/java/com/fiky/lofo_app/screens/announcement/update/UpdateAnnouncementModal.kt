package com.fiky.lofo_app.screens.announcement.update

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fiky.lofo_app.data.models.AnnouncementModel
import com.fiky.lofo_app.screens.announcement.create.AnnouncementTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateAnnouncementModal(
    announcement: AnnouncementModel,
    onDismiss: () -> Unit,
    onUpdateSubmit: (title: String, location: String, lostAt: String, description: String) -> Unit,
    isUpdating: Boolean
) {
    // MENYUAPI DATA: Inisialisasi form state langsung menggunakan data mentah halaman detail
    var title by remember { mutableStateOf(announcement.title) }
    var location by remember { mutableStateOf(announcement.location) }
    var description by remember { mutableStateOf(announcement.description) }
    var dateLost by remember { mutableStateOf(announcement.createdAt) } // Default fallback

    var tempDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // DATE PICKER DI DALAM MODAL
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                            .format(java.util.Date(millis))
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

    // TIME PICKER DI DALAM MODAL
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(is24Hour = true)
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val formattedTime = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                    val localDateTimeString = "$tempDate $formattedTime"

                    try {
                        val localFormat = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
                        val localDate = localFormat.parse(localDateTimeString)
                        val utcFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.US).apply {
                            timeZone = java.util.TimeZone.getTimeZone("UTC")
                        }
                        dateLost = utcFormat.format(localDate!!)
                    } catch (e: Exception) {
                        dateLost = localDateTimeString
                    }
                    showTimePicker = false
                }) { Text("Selesai") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Batal") }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Pilih Jam")
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
            },
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        // KUNCI MATI: User dipaksa tidak bisa menutup modal via tombol back HP
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false
        ),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Form dengan tombol X eksklusif
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.EditCalendar, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Perbarui Pengumuman", style = MaterialTheme.typography.titleMedium)
                    Text("Ubah detail informasi laporan barang", style = MaterialTheme.typography.bodySmall)
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            // Input Fields yang sudah terisi otomatis
            AnnouncementTextField(
                label = "Judul Laporan",
                value = title,
                onValueChange = { title = it },
                placeholder = "Ubah judul barang..."
            )

            AnnouncementTextField(
                label = "Lokasi Terakhir",
                value = location,
                onValueChange = { location = it },
                placeholder = "Ubah nama ruangan/gedung...",
                leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = MaterialTheme.colorScheme.primary) }
            )

            // Input Tanggal Hilang Baru
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Waktu Kejadian Baru", fontWeight = FontWeight.Bold)
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
                        Text(text = dateLost.take(16).replace("T", " "), color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }

            AnnouncementTextField(
                label = "Deskripsi Tambahan",
                value = description,
                onValueChange = { description = it },
                placeholder = "Perbarui ciri fisik barang...",
                singleLine = false,
                minLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tombol Simpan Perubahan
            Button(
                onClick = { onUpdateSubmit(title, location, dateLost, description) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = title.isNotBlank() && location.isNotBlank() && description.isNotBlank()
            ) {
                if (isUpdating) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Simpan Perubahan", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Default.Save, null, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}