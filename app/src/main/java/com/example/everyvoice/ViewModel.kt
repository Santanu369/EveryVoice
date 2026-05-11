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
import com.example.everyvoice.VoiceToText.VoiceCallback
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
import org.json.JSONObject

class MainViewModel(
    private val textRepository: TextRepository = Graph.textRepository
): ViewModel(), VoiceCallback {

    private val _state = MutableStateFlow(VoiceToTextParserState())
    val state = _state.asStateFlow()

    var isEdit by mutableStateOf(false)
    var _id by mutableStateOf(0)
    var _title by mutableStateOf("")
    var _text by mutableStateOf("")

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

    var socketData = mutableStateOf("")
    var socketDataOCR = mutableStateOf("")

    var connectionStatus = mutableStateOf("Disconnected")

    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()

    fun connectToWebSocket() {
        val request = okhttp3.Request.Builder()
            .url("ws://192.168.1.8:8765") // Replace with your URL
            .build()

        val listener = MyWebSocketListener(
            onMessageReceived = { text ->
                val json = JSONObject(text)
                // type detection
                if (json.getString("type") == "ASL") {
                    val char = json.getString("char")

                    socketData.value += char
                    latestWord.value += char

                    if (char == "_") {
                        socketData.value = socketData.value.dropLast(2)
                        latestWord.value = latestWord.value.dropLast(2)
                    }

                }
                else if (json.getString("type") == "OCR") {
                    socketDataOCR.value = json.getString("text")
                }
                else {
                    socketData.value += "no text lab"
                }
            },
            onStatusChange = { status ->
                connectionStatus.value = status
            }
        )

        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(mode: String) {
        val json = JSONObject()
        json.put("action", "set_mode")
        json.put("mode", mode)

        webSocket?.send(json.toString())
    }

    override fun onCleared() {
        super.onCleared()
        // Close connection when ViewModel is destroyed
        webSocket?.close(1000, "App closing")

    }

}
