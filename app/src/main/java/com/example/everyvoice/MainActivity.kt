package com.example.everyvoice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.everyvoice.ImageRecognizer.ImageDetectionGemini
import com.example.everyvoice.ImageRecognizer.ImgDetectionViewModel
import com.example.everyvoice.OCR.OCRcameraXScreen
import com.example.everyvoice.TextToSpeech.data.Graph
import com.example.everyvoice.TextToSpeech.ttsUi.AddTextScreen
import com.example.everyvoice.TextToSpeech.ttsUi.TextToSpeechGen
import com.example.everyvoice.TextToSpeech.ttsUi.TextToSpeechScreen
import com.example.everyvoice.Utils.rememberTTS
import com.example.everyvoice.VoiceToText.VoiceToTextScreen
import com.example.everyvoice.ui.theme.EveryVoiceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Graph.provide(this)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val ImgViewModel: ImgDetectionViewModel = viewModel()
            EveryVoiceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        paddingValues = innerPadding,
                        viewModel = viewModel,
                        ImgViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun NavHost(paddingValues: PaddingValues,
            viewModel: MainViewModel,
            imgDetectViewModel: ImgDetectionViewModel) {

    val context = LocalContext.current
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {

            HomePage(
                navController = navController
            )
        }

        composable("speechToText") {

            VoiceToTextScreen(context, viewModel = viewModel)
        }

        composable("textToSpeech") {

            TextToSpeechScreen(viewModel, navController)
        }

        composable("addTextScreen") {

            AddTextScreen(viewModel, navController)
        }

        composable("textToSpeechGen/{title}/{text}") {
            val title = it.arguments?.getString("title")?: ""
            val text = it.arguments?.getString("text")?: ""

            TextToSpeechGen(title, text)
        }

        composable("signLangTTS") {

            SignLangSST()
//            KtorWebSocketTest()
        }

        composable("imgLabelingScreen") {
//              ImgDescriberScreen()
//            ImgLabelingCameraXScreen()
//            ImgLabelingScreen()
            ImageDetectionGemini(imgDetectViewModel)
        }

        composable("textRecognizerScreen") {
//              ImgDescriberScreen()
            OCRcameraXScreen()
        }

//        composable("emergencyScreen") {
//            EmergencyScreen()
//        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    EveryVoiceTheme {
////        Greeting("Android")
////        HomePage()
//        HomePage()
//    }
//}