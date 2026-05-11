package com.example.everyvoice.ImageRecognizer

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.everyvoice.Utils.CameraPreview
import com.example.everyvoice.Utils.rememberTTS
import com.example.everyvoice.VoiceToText.VoiceRecog
import com.example.everyvoice.ui.theme.MinecraftFont

@Composable
fun ImageDetectionGemini(viewModel: ImgDetectionViewModel) {
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }


    val CAMERAX_PERMISSION = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )
    fun hasPermissions(): Boolean {
        return CAMERAX_PERMISSION.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    if (!hasPermissions()) {
        ActivityCompat.requestPermissions(
            context as Activity, CAMERAX_PERMISSION, 0
        )
    }

    ImgDetectionCameraUi(context, controller, viewModel)
}

@Composable
fun ImgDetectionCameraUi(
    context: Context,
    cameraController: LifecycleCameraController,
    viewModel: ImgDetectionViewModel
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


    val description by viewModel.imageDescription.collectAsState()

    val tts = rememberTTS()
    var text by remember { mutableStateOf("Click Anywhere on the screen to open camera") }

    LaunchedEffect(description) {
        tts.speak(description)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                if (viewModel.currentTime - viewModel.latTime < 6000) {
                    viewModel.latTime = viewModel.currentTime
                }
                else if (state.isSpeaking) {
                    voiceToTextParser.stopListing()
                }
                else {
                    voiceToTextParser.startListening()

                    // Take Photo
                    takePhotoImgDetection(context, cameraController) {
                        viewModel.imageBitmap.value = it
                    }
                }
            }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 25.dp)) {

            CameraPreview(
                controller = cameraController,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )

//            Spacer(modifier = Modifier.height(125.dp))

            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(text = description, color = Color.Green, fontSize = 16.sp, fontFamily = MinecraftFont)
            }

        }
    }
}

fun takePhotoImgDetection(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val bitmap = image.toBitmap()
                onPhotoTaken(bitmap)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("camera", "Could't take photo", exception)
            }
        }
    )
}