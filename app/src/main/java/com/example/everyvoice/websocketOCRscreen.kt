package com.example.everyvoice

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.everyvoice.Utils.rememberTTS

@Composable
fun websocketORCscreen(viewModel: MainViewModel = viewModel()) {
    val data by viewModel.socketDataOCR
    val status by viewModel.connectionStatus

    val tts = rememberTTS()

    LaunchedEffect(viewModel.socketDataOCR.value) {
        tts.speak(viewModel.socketDataOCR.value)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.socketDataOCR.value = ""
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .clickable {
                //            viewModel.latestWord.value = ""
//            viewModel.socketData.value = ""
//            viewModel.socketDataOCR.value = ""
                viewModel.connectToWebSocket()
                viewModel.sendMessage("ocr")
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Status: $status", color = Color.Green)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Received Data:",
            fontWeight = FontWeight.Bold
        )
        Text(text = viewModel.socketDataOCR.value, fontSize = 24.sp, color = Color.Blue)

//        Button(onClick = {
////            viewModel.latestWord.value = ""
////            viewModel.socketData.value = ""
////            viewModel.socketDataOCR.value = ""
//            viewModel.connectToWebSocket()
//            viewModel.sendMessage("ocr")
//        }) {
//            Text("Connect")
//        }
    }
}
