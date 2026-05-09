package com.example.everyvoice.TextToSpeech.ttsUi

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.everyvoice.Utils.rememberTTS

@Composable
fun TextToSpeechGen(title: String = "", text: String = "") {

    val tts = rememberTTS()

    // UI
    Column(modifier = Modifier
        .background(Color.Black)
        .padding(50.dp)
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(modifier = Modifier.fillMaxSize()
            .border(width = 3.dp, color = Color.DarkGray),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Row(modifier = Modifier.fillMaxWidth()
                .padding(32.dp),
                horizontalArrangement = Arrangement.Center) {
                Text(title, color = Color.White, fontSize = 24.sp, fontFamily = FontFamily.Monospace)
            }

            Row(modifier = Modifier.fillMaxWidth()
                .padding(32.dp),
                horizontalArrangement = Arrangement.Center) {
                Text(text, color = Color.White, fontSize = 24.sp, fontFamily = FontFamily.Monospace)
            }

            Spacer(modifier = Modifier.height(100.dp));

            Button(
                shape = ButtonDefaults.filledTonalShape,
                onClick = {
                    tts.speak(text)
                }
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "play",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp))
            }
        }

    }
}