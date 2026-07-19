package com.example.everyvoice.VideoCall

import android.util.Log
import androidx.compose.runtime.*

@Composable
fun VideoCallApp() {

    var isCallActive by remember { mutableStateOf(false) }
    var remoteUserName by remember { mutableStateOf("") }
    var hasPermissions by remember { mutableStateOf(false) }


    if (!hasPermissions) {
        RequestCameraAndMicPermissions {
            hasPermissions = true
        }
    } else {
        Log.d("ws", "else")

        when {
            isCallActive -> {
                VideoCallScreen(
                    remoteUserName = remoteUserName,
                    onEndCall = {
                        isCallActive = false
                        remoteUserName = ""
                    }
                )
            }
            else -> {
                CallInitiationScreen(
                    onStartCall = { name ->
                        remoteUserName = name
                        isCallActive = true
                    }
                )
            }
        }
    }
}