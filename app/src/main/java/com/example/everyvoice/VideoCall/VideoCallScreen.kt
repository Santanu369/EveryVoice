package com.example.everyvoice.VideoCall

// VideoCallScreen.kt
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import android.view.SurfaceView
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import io.agora.rtc.RtcEngine

@Composable
fun VideoCallScreen(
    remoteUserName: String,
    viewModel: VideoCallViewModel = viewModel(),
    onEndCall: () -> Unit
) {
    val context = LocalContext.current
    val callState by viewModel.callState.collectAsState()
    val agoraManager = remember {
        AgoraManager(
            context,
            "43b1098cd6914a7393143151c6378e78",
        )
    }

    LaunchedEffect(Unit) {
        agoraManager.setupRtcEngine()
        agoraManager.joinChannel("video-call", 0)
    }

    DisposableEffect(Unit) {
        onDispose {
            agoraManager.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Remote Video (Full Screen)
        AndroidView(
            factory = { context ->
                SurfaceView(context).apply {
                    agoraManager.setupRemoteVideo(this, 1)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Local Video (Picture in Picture)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(120.dp)
                .padding(16.dp)
                .background(Color.Gray)
        ) {
            AndroidView(
                factory = { context ->
                    SurfaceView(context).apply {
                        agoraManager.setupLocalVideo(this)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Call Info (Top)
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = remoteUserName,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Connected",
                color = Color.Green,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Control Buttons (Bottom)
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mute Button
            IconButton(
                onClick = { viewModel.toggleMute() },
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        if (callState.isMuted) Color.Red else Color.Gray,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (callState.isMuted)
                        Icons.Filled.MicOff else Icons.Filled.Mic,
                    contentDescription = "Mute",
                    tint = Color.White
                )
            }

            // End Call Button
            IconButton(
                onClick = {
                    viewModel.endCall()
                    onEndCall()
                },
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.Red, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = "End Call",
                    tint = Color.White
                )
            }

            // Camera Toggle Button
            IconButton(
                onClick = { viewModel.toggleCamera() },
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        if (callState.isCameraOff) Color.Red else Color.Gray,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (callState.isCameraOff)
                        Icons.Filled.VideocamOff else Icons.Filled.Videocam,
                    contentDescription = "Camera",
                    tint = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun VideoCallScreenPreview() {
    VideoCallScreen(remoteUserName = "John Doe", onEndCall = {})
}