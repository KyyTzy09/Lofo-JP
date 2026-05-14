package com.fiky.lofo_app.screens.voice

data class VoiceState(
    val transcript: String = "",
    val isListening: Boolean = false,
    val error: String? = null
)