package com.example.everyvoice.TextToSpeech.data

import android.content.Context
import androidx.room.Room

object Graph {
    lateinit var database: TextDataBase

    val textRepository by lazy {
        TextRepository(database.dao)
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, TextDataBase::class.java, "text.db")
            .build()
    }
}