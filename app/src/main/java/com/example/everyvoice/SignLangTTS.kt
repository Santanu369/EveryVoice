package com.example.everyvoice

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.everyvoice.Utils.rememberTTS

@Composable
fun SignLangSST(viewModel: MainViewModel = viewModel()) {
    val data by viewModel.socketData
    val status by viewModel.connectionStatus

    val tts = rememberTTS()

    LaunchedEffect(viewModel.latestWord.value) {
        if (viewModel.latestWord.value.endsWith(" ")) {

            tts.speak("${viewModel.latestWord.value}")
//            Log.d("ws", "LaunchedEffTriggered")

            viewModel.latestWord.value = ""
        }
        else if (viewModel.latestWord.value.endsWith("_")) {
            viewModel.socketData.value.dropLast(1)
            viewModel.latestWord.value.dropLast(1)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Status: $status", color = Color.Green)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Received Data:",
            fontWeight = FontWeight.Bold
        )
        Text(text = data, fontSize = 24.sp, color = Color.Blue)

        Button(onClick = { viewModel.connectToWebSocket() }) {
            Text("Connect")
        }
    }
}
