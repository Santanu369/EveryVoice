package com.example.everyvoice.Utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberTTS(): TTSManager {
    val context = LocalContext.current

    val ttsManager = remember {
        TTSManager(context)
    }

    DisposableEffect(Unit) {
        onDispose {
            ttsManager.shutdown()
        }
    }

    return ttsManager
}
