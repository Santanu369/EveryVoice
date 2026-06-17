package com.example.everyvoice.VoiceToText

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.everyvoice.MainViewModel

@Composable
fun VoiceToTextScreen(
    context: Context,
    viewModel: MainViewModel
) {
    val voiceToTextParser by lazy {
        VoiceRecog(context, viewModel)
    }

    var canRecord by remember {
        mutableStateOf(false)
    }

    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {isGranted ->
            canRecord = isGranted
        }
    )

    LaunchedEffect(recordAudioLauncher) {
        recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    val state by viewModel.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.width(360.dp).height(300.dp),
                onClick = {
                    if (state.isSpeaking) {
                        voiceToTextParser.stopListing()
                    }
                    else {
                        voiceToTextParser.startListening()
                    }
                }
            ) {
                AnimatedContent(state.isSpeaking) { isSpeaking ->
                    if (isSpeaking) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Stop",
                            modifier = Modifier.width(160.dp).height(130.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Mic,
                            contentDescription = "Mic",
                            modifier = Modifier.width(160.dp).height(130.dp)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(200.dp))

            AnimatedContent(state.isSpeaking) {
                if (it) {
                    Text(text = "Speaking...", fontFamily = FontFamily.Monospace, fontSize = 24.sp)
                } else {
                    Text(text = state.spokenText.ifEmpty { "Click on mic" },
                        fontFamily = FontFamily.Monospace,
                        fontSize = 24.sp)
                }
            }

        }
    }

}