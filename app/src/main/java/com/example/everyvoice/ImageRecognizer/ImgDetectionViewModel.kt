package com.example.everyvoice.ImageRecognizer

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everyvoice.BuildConfig
import com.example.everyvoice.VoiceToText.VoiceCallback
import com.example.everyvoice.VoiceToText.VoiceToTextParserState
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ImgDetectionViewModel: ViewModel(), VoiceCallback {

    val imageBitmap = mutableStateOf<Bitmap?>(null)

    // Voice to Text

    private val _state = MutableStateFlow(VoiceToTextParserState())
    val state = _state.asStateFlow()

    var latTime = 0L
    val currentTime = System.currentTimeMillis()

    override fun startUpdate() {
        _state.update { VoiceToTextParserState() }
    }

    override fun notRecognizing() {
        _state.update {
            it.copy(
                error = "Recognition is not available."
            )
        }
    }

    override fun speaking() {
        _state.update {
            it.copy(
                isSpeaking = true
            )
        }
    }

    override fun stopListening() {
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }
    }

    override fun onReadyForSpeech() {
        _state.update {
            it.copy(
                error = null
            )
        }
    }

    override fun endOfSpeech() {
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }
    }

    override fun onError(error: Int) {
        _state.update {
            it.copy(
                error = "Error = $error"
            )
        }
    }

    override fun onResult(result: String) {
        _state.update {
            it.copy(
                spokenText = result
            )
        }

        imageDescription.value = "Analysing Image..."
        if (imageBitmap.value != null) {
            describeImage(
                bitmap = imageBitmap.value!!,
                promptText = result
            )
        }
    }



    // Image describer
    private val apiKey = BuildConfig.GEMINI_API_KEY
    // State to hold the description
    val imageDescription = MutableStateFlow("")

    // Initialize the Gemini Model
    // Note: In a real app, don't hardcode the API key here. Use BuildConfig or secrets.
    private val generativeModel = GenerativeModel(
        modelName = "gemini-3-flash-preview", // "flash" is faster and free
        apiKey = apiKey
    )


    fun describeImage(bitmap: Bitmap, promptText: String = "describe the image") {

        viewModelScope.launch {
            try {
                val prompt = "You are an assistive image description system for visually impaired users.\n" +
                        "\n" +
                        "Rules:\n" +
                        "- Be accurate and helpful\n" +
                        "- Do not over-explain too deeply until user means something like that\n" +
                        "- Do not under-describe important objects\n" +
                        "- Focus on what matters for understanding the scene\n" +
                        "\n" +
                        "User request:\n" +
                        promptText

                val inputContent = content {
                    image(bitmap)
                    text(prompt)
                }

                val response = generativeModel.generateContent(inputContent)
                imageDescription.value = response.text ?: "Could not describe image."
            } catch (e: Exception) {
                imageDescription.value = "Error: ${e.localizedMessage}"
            }
        }
    }

}