package com.fiky.lofo_app.screens.voice

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.fiky.lofo_app.ui.theme.Error
import com.fiky.lofo_app.ui.theme.OnPrimary
import com.fiky.lofo_app.ui.theme.OnSurface
import com.fiky.lofo_app.ui.theme.OnSurfaceVariant
import com.fiky.lofo_app.ui.theme.Outline
import com.fiky.lofo_app.ui.theme.OutlineVariant
import com.fiky.lofo_app.ui.theme.Primary
import com.fiky.lofo_app.ui.theme.PrimaryContainer
import com.fiky.lofo_app.ui.theme.SurfaceContainer
import com.fiky.lofo_app.ui.theme.SurfaceContainerHigh
import com.fiky.lofo_app.ui.theme.SurfaceContainerLow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceCommandModal(
    onDismiss: () -> Unit,
    onSend: (text: String, connectItem: Boolean) -> Unit,
    isLoading: Boolean
) {
    var transcript by remember { mutableStateOf("") }
    var connectItem by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Inisialisasi parser dan langsung pasang callback-nya di sini
    val parser = remember {
        VoiceToTextParser(context).apply {
            onResultCallback = { newVoiceText ->
                // AKUMULASI: Tambahkan hasil suara ke teks yang sudah ada (atau yang sudah diketik)
                transcript = if (transcript.isEmpty()) {
                    newVoiceText
                } else {
                    "$transcript $newVoiceText"
                }
            }
        }
    }

    val state = parser.state

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceContainer,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Outline) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header & Instruction
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Mic, contentDescription = null, tint = Primary)
                Spacer(Modifier.width(8.dp))
                Text("AI Voice Announcement", color = OnSurface, style = MaterialTheme.typography.titleMedium)
            }
            Text(
                "Ucapkan detail atau ketik manual",
                color = OnSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.height(32.dp))

            // Mic Button
            Box(contentAlignment = Alignment.Center) {
                FloatingActionButton(
                    onClick = {
                        if (state.isListening) parser.stopListening() else parser.startListening()
                    },
                    containerColor = if (state.isListening) Error else Primary,
                    shape = CircleShape
                ) {
                    Icon(
                        if (state.isListening) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = null,
                        tint = OnPrimary
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // 3. Transcript Box (Sekarang Editable)
            OutlinedTextField(
                value = transcript, // Gunakan state lokal
                onValueChange = { newValue ->
                    transcript = newValue // Sekarang user bisa ngetik/edit di sini
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                placeholder = { Text("Mulai bicara atau ketik di sini...", color = Outline) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceContainerLow,
                    unfocusedContainerColor = SurfaceContainerLow,
                    focusedBorderColor = Primary, // Beri warna saat diedit agar jelas
                    unfocusedBorderColor = OutlineVariant,
                    focusedTextColor = OnSurface,
                    unfocusedTextColor = OnSurface
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Option: Connect Item
            Surface (
                onClick = { connectItem = !connectItem },
                color = if (connectItem) PrimaryContainer else SurfaceContainerHigh,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = connectItem,
                        onCheckedChange = { connectItem = it },
                        colors = CheckboxDefaults.colors(checkedColor = Primary)
                    )
                    Text("Hubungkan dengan item saya?", color = OnSurface)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Action Buttons
            Button(
                onClick = { onSend(transcript, connectItem) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                enabled = transcript.isNotBlank() // Aktif jika ada teks
            ) {
                if (isLoading) CircularProgressIndicator() else Text("Kirim ke AI", color = OnPrimary)
            }
        }
    }
}