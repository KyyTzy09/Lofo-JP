package com.fiky.lofo_app.screens.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


class VoiceToTextParser(private val context: Context) : RecognitionListener {
    var state by mutableStateOf(VoiceState())
        private set

    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    var onResultCallback: ((String) -> Unit)? = null
    fun startListening() {
        state = VoiceState(isListening = true)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID") // Set Bahasa Indonesia
        }
        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)
    }

    fun stopListening() = recognizer.stopListening()

    override fun onResults(results: Bundle?) {
        results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)?.let { text ->
            // 1. Update state internal
            state = state.copy(transcript = text, isListening = false)

            // 2. Panggil callback (ini yang akan kita pakai di UI)
            onResultCallback?.invoke(text)

            // 3. Reset transcript di state internal supaya jika user ngomong hal yang sama lagi,
            // LaunchedEffect di UI tetap bisa mendeteksi perubahan dari "" ke "teks".
            state = state.copy(transcript = "")
        }
    }
    // Implementasi callback listener lainnya (onReadyForSpeech, onError, dll) disingkat...
    override fun onReadyForSpeech(params: Bundle?) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEndOfSpeech() { state = state.copy(isListening = false) }
    override fun onError(error: Int) { state = state.copy(error = "Error: $error", isListening = false) }
    override fun onPartialResults(partialResults: Bundle?) {}
    override fun onEvent(eventType: Int, params: Bundle?) {}
}
