package com.example.everyvoice.VoiceToText

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.everyvoice.MainViewModel

private val BgBlack = Color(0xFF0B0D12)
private val SurfaceDark = Color(0xFF171A21)
private val SurfaceBorder = Color(0xFF2A2E38)
private val AccentPrimary = Color(0xFF7C8CFF)
private val DangerRed = Color(0xFFFF6B6B)
private val TextPrimary = Color(0xFFF3F4F8)
private val TextSecondary = Color(0xFF9AA0AE)

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
        containerColor = BgBlack,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                containerColor = if (state.isSpeaking) DangerRed else AccentPrimary,
                contentColor = Color.Black,
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
                            modifier = Modifier.size(48.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Mic,
                            contentDescription = "Mic",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BgBlack)
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(80.dp))

            AnimatedContent(state.isSpeaking) { isSpeaking ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSpeaking) DangerRed.copy(alpha = 0.15f) else SurfaceDark)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (isSpeaking) "● Listening" else "Idle",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isSpeaking) DangerRed else TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceDark)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(state.isSpeaking) {
                    if (it) {
                        Text(
                            text = "Speaking...",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 22.sp,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            text = state.spokenText.ifEmpty { "Tap the mic to start" },
                            fontFamily = FontFamily.Monospace,
                            fontSize = 20.sp,
                            color = if (state.spokenText.isEmpty()) TextSecondary else TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

}