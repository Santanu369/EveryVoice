package com.example.everyvoice.TextToSpeech.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.everyvoice.TextToSpeech.data.TextDao

@Database(
    entities = [TextData::class],
    version = 1
)
abstract class TextDataBase: RoomDatabase() {
    abstract val dao: TextDao
}