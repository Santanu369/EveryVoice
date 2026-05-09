package com.example.everyvoice.TextToSpeech.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "textData")
data class TextData(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String = "",

    val text: String = "",

    val tag: String? = null
)