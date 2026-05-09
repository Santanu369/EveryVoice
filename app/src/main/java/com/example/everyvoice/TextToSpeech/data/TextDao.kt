package com.example.everyvoice.TextToSpeech.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TextDao {

    @Upsert
    suspend fun upsertText(textDao: TextData)

    @Delete
    suspend fun deleteText(textData: TextData)

    @Query("SELECT * FROM textData")
    fun getText(): Flow<List<TextData>>
}