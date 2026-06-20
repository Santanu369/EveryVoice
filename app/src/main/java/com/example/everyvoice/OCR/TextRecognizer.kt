package com.example.everyvoice.OCR

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.request.Disposable
import com.example.everyvoice.Utils.CameraPreview
import com.example.everyvoice.Utils.rememberTTS
import com.example.everyvoice.ui.theme.MinecraftFont
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

private val AccentBlue = Color(0xFF0050C8)

@Composable
fun OCRcameraXScreen() {
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

    OCRCameraUi(context, controller)

    DisposableEffect(Unit) {
        onDispose {
            controller.unbind()
        }
    }
}

@Composable
fun OCRCameraUi(
    context: Context,
    cameraController: LifecycleCameraController
) {
    val tts = rememberTTS()
    var text by remember { mutableStateOf("Click Anywhere on the screen to open camera") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()
        ) {

            CameraPreview(
                controller = cameraController,
                modifier = Modifier.fillMaxSize()
            )

            Spacer(modifier = Modifier.height(125.dp))

            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(text = text, color = Color.Green, fontSize = 16.sp, fontFamily = MinecraftFont)
            }

        }

        // ocr text preview over the Camera view
        Box(modifier = Modifier.verticalScroll(rememberScrollState())
            .padding(start = 50.dp, end = 50.dp, top = 50.dp)
            .fillMaxSize(),
            contentAlignment = Alignment.TopCenter) {
            Text(text = text, color = Color.Green, fontSize = 16.sp, fontFamily = MinecraftFont)
        }

        // button to take photo
        Box(modifier = Modifier.fillMaxSize()
            .padding(bottom = 15.dp),
            contentAlignment = Alignment.BottomCenter) {
            Row(modifier = Modifier.fillMaxWidth()
                .align(Alignment.BottomEnd),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    modifier = Modifier.width(150.dp)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentBlue,
                        contentColor = Color.White
                    ),
                    onClick = {
                        takePhoto(context, cameraController) {
                            val image = InputImage.fromBitmap(it, 0)
                            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

                            recognizer.process(image).addOnSuccessListener { ocrText ->
                                Log.d("ws", ocrText.text)
                                text = ocrText.text
                                tts.speak(text)
                            }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Failed to recognize picture: ${e.message}", Toast.LENGTH_SHORT).show()
                                    tts.speak("Unable to capture Image")
                                }
                        }
                    }
                ) {
                    Text("Take picture")
                }

                Button(
                    modifier = Modifier.width(150.dp)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    onClick = {}
                ) {
                    Text("Clear Text")
                }
            }
        }
    }
}

fun takePhoto(
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



//@Preview(showBackground = true)
//@Composable
//fun PreviewTR() {
//    TextRecognizerScreen()
//}