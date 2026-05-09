package com.example.everyvoice.TextToSpeech.data

import com.example.everyvoice.TextToSpeech.data.TextDao
import com.example.everyvoice.TextToSpeech.data.TextData

class TextRepository(
    private val textDao: TextDao
) {

    suspend fun upsert(textData: TextData) {
        textDao.upsertText(textData)
    }

    suspend fun delete(textData: TextData) {
        textDao.deleteText(textData)
    }

    fun getAll() = textDao.getText()
}