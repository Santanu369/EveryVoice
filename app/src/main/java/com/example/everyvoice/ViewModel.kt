package com.example.everyvoice

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everyvoice.TextToSpeech.data.Graph
import com.example.everyvoice.TextToSpeech.data.TextData
import com.example.everyvoice.TextToSpeech.data.TextRepository
import com.example.everyvoice.Utils.MyWebSocketListener
import com.example.everyvoice.VoiceToText.VoiceToTextParserState
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.WebSocket
import okhttp3.OkHttpClient
import com.google.ai.client.generativeai.type.content

class MainViewModel(
    private val textRepository: TextRepository = Graph.textRepository
): ViewModel() {

    private val _state = MutableStateFlow(VoiceToTextParserState())
    val state = _state.asStateFlow()

    var isEdit by mutableStateOf(false)
    var _id by mutableStateOf(0)
    var _title by mutableStateOf("")
    var _text by mutableStateOf("")

    fun startUpdate() {
        _state.update { VoiceToTextParserState() }
    }

    fun notRecognizing() {
        _state.update {
            it.copy(
                error = "Recognition is not available."
            )
        }
    }

    fun speaking() {
        _state.update {
            it.copy(
                isSpeaking = true
            )
        }
    }

    fun stopListening() {
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }
    }

    fun onReadyForSpeech() {
        _state.update {
            it.copy(
                error = null
            )
        }
    }

    fun endOfSpeech() {
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }
    }

    fun onError(error: Int) {
        _state.update {
            it.copy(
                error = "Error = $error"
            )
        }
    }

    fun onResult(result: String) {
        _state.update {
            it.copy(
                spokenText = result
            )
        }
    }

    // ROOM DATABASE

    lateinit var allText: Flow<List<TextData>>

    init {
        viewModelScope.launch(Dispatchers.IO) {
            allText = textRepository.getAll()
        }
    }

    fun deleteText(textData: TextData) {
        viewModelScope.launch(Dispatchers.IO) {
            textRepository.delete(textData)
        }
    }

    fun upsertText(textData: TextData) {
        viewModelScope.launch(Dispatchers.IO) {
            textRepository.upsert(textData)
        }
    }


    // Holds the latest received message
    var latestWord = mutableStateOf("")

    var socketData = mutableStateOf("No data yet")
    var connectionStatus = mutableStateOf("Disconnected")

    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()

    fun connectToWebSocket() {
        val request = okhttp3.Request.Builder()
            .url("ws://example.com/websocket") // Replace with your URL
            .build()

        val listener = MyWebSocketListener(
            onMessageReceived = { text ->
                viewModelScope.launch {
                    socketData.value += text
                    latestWord.value += text
                }
            },
            onStatusChange = { status ->
                connectionStatus.value = status
            }
        )

        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    override fun onCleared() {
        super.onCleared()
        // Close connection when ViewModel is destroyed
        webSocket?.close(1000, "App closing")

    }


    private val apiKey = BuildConfig.GEMINI_API_KEY
    // State to hold the description
    val imageDescription = MutableStateFlow("")

    // Initialize the Gemini Model
    // Note: In a real app, don't hardcode the API key here. Use BuildConfig or secrets.
    private val generativeModel = GenerativeModel(
        modelName = "gemini-3-flash-preview", // "flash" is faster and free
        apiKey = apiKey
    )

    fun describeImage(bitmap: Bitmap) {
        viewModelScope.launch {
            try {
                val prompt = "Describe this image in detail for a text-to-speech application."

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
