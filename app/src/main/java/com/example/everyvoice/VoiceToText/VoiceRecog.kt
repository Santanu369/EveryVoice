package com.example.everyvoice.VoiceToText

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.example.everyvoice.MainViewModel

class VoiceRecog(
    private val context: Context,
    private val viewModel1: MainViewModel
): RecognitionListener {

    val _state = viewModel1.state

    val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    fun startListening(languageCode: String = "en") {
        viewModel1.startUpdate()

        if(!SpeechRecognizer.isRecognitionAvailable(context)) {
            viewModel1.notRecognizing()
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        }

        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)

        viewModel1.speaking()
    }

    fun stopListing() {
        viewModel1.stopListening()

        recognizer.stopListening()
    }

    override fun onReadyForSpeech(p0: Bundle?) {
        viewModel1.onReadyForSpeech()
    }

    override fun onBeginningOfSpeech() = Unit

    override fun onBufferReceived(p0: ByteArray?) = Unit

    override fun onEndOfSpeech() {
        viewModel1.endOfSpeech()
    }

    override fun onError(error: Int) {
        viewModel1.onError(error)
    }

    override fun onEvent(p0: Int, p1: Bundle?) = Unit

    override fun onPartialResults(results: Bundle?) = Unit

    override fun onResults(results: Bundle?) {
        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let {  result ->
                 viewModel1.onResult(result)
            }
    }

    override fun onRmsChanged(p0: Float) = Unit

}

data class VoiceToTextParserState(
    val spokenText: String = "",
    val isSpeaking: Boolean = false,
    val error: String? = null,
)