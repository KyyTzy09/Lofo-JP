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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.fiky.lofo_app.ui.theme.*
import com.fiky.lofo_app.utils.TextLimiter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceCommandModal(
    onDismiss: () -> Unit,
    onSend: (text: String, connectItem: Boolean) -> Unit,
    isLoading: Boolean
) {
    var transcript by remember { mutableStateOf("") }
    var connectItem by remember { mutableStateOf(false) }

    val wordCount = if (transcript.isBlank()) 0 else transcript.trim().split(Regex("\\s+")).size
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState() // State untuk handle scroll kontainer form

    val parser = remember {
        VoiceToTextParser(context).apply {
            onResultCallback = { newVoiceText ->
                val combinedText = if (transcript.isEmpty()) {
                    newVoiceText
                } else {
                    "$transcript $newVoiceText"
                }

                transcript = TextLimiter(combinedText)
            }
        }
    }

    val state = parser.state

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceContainer,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Outline) },
        // JURUS KUNCI: Matikan dismiss bawaan tombol back di sini
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(scrollState), // MEMBUAT MODAL BISA DI-SCROLL
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section dengan Tombol Tutup (X)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Mic, contentDescription = null, tint = Primary)
                Spacer(Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("AI Voice Announcement", color = OnSurface, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Ucapkan detail atau ketik manual",
                        color = OnSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                // Tombol Close (X) eksklusif sebagai pengganti back press
                IconButton(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onDismiss()
                    }
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = OnSurfaceVariant)
                }
            }

            Spacer(Modifier.height(24.dp))

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

            // Transcript Box (Editable)
            OutlinedTextField(
                value = transcript,
                onValueChange = { newValue ->
                    transcript = TextLimiter(newValue)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                placeholder = { Text("Mulai bicara atau ketik di sini...", color = Outline) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceContainerLow,
                    unfocusedContainerColor = SurfaceContainerLow,
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = OutlineVariant,
                    focusedTextColor = OnSurface,
                    unfocusedTextColor = OnSurface
                ),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$wordCount / 200 kata",
                    color = if (wordCount >= 200) Error else OnSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }

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
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onSend(transcript, connectItem)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                enabled = transcript.isNotBlank()
            ) {
                if (isLoading) CircularProgressIndicator() else Text("Kirim ke AI", color = OnPrimary)
            }
        }
    }
}