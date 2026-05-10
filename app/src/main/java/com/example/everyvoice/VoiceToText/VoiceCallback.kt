package com.example.everyvoice.VoiceToText

interface VoiceCallback {
    fun startUpdate()
    fun notRecognizing()
    fun speaking()
    fun stopListening()
    fun onReadyForSpeech()
    fun endOfSpeech()
    fun onResult(text: String)
    fun onError(error: Int)

}