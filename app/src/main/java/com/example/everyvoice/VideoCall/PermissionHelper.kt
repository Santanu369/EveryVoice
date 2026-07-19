package com.example.everyvoice.VideoCall

// PermissionHelper.kt
import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun RequestCameraAndMicPermissions(onPermissionsGranted: () -> Unit) {
    Log.d("ws", "requestcamera")
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Check if both permissions were granted
        val cameraGranted = permissions[Manifest.permission.CAMERA] == true
        val audioGranted = permissions[Manifest.permission.RECORD_AUDIO] == true

        if (cameraGranted && audioGranted) {
            Log.d("ws", "onPermissionGranted")
            onPermissionsGranted()
        } else {
            Log.d("ws", "Permissions denied")
            // Handle denial (e.g., show a placeholder UI)
        }
    }

    // 2. Trigger Safely using LaunchedEffect (Runs ONLY ONCE when entering composition)
    LaunchedEffect(Unit) {
        launcher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        )
    }
}