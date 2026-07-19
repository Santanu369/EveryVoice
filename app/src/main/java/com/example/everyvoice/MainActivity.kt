package com.example.everyvoice

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.everyvoice.Authentication.LogInScreen
import com.example.everyvoice.Authentication.SignUpScreen
import com.example.everyvoice.ImageRecognizer.ImageDetectionGemini
import com.example.everyvoice.ImageRecognizer.ImgDetectionViewModel
import com.example.everyvoice.OCR.OCRcameraXScreen
import com.example.everyvoice.TextToSpeech.data.Graph
import com.example.everyvoice.TextToSpeech.ttsUi.AddTextScreen
import com.example.everyvoice.TextToSpeech.ttsUi.TextToSpeechGen
import com.example.everyvoice.TextToSpeech.ttsUi.TextToSpeechScreen
import com.example.everyvoice.VideoCall.VideoCallApp
import com.example.everyvoice.VoiceToText.VoiceToTextScreen
import com.example.everyvoice.ui.theme.EveryVoiceTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private val auth = FirebaseAuth.getInstance()
    private var startingDestination = "signUpScreen"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Graph.provide(this)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val ImgViewModel: ImgDetectionViewModel = viewModel()

            if (auth.currentUser != null) {
                startingDestination = "home"
            }
            EveryVoiceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        paddingValues = innerPadding,
                        viewModel = viewModel,
                        ImgViewModel,
                        startingDestination
                    )
                }
            }
        }
    }

}

@Composable
fun NavHost(paddingValues: PaddingValues,
            viewModel: MainViewModel,
            imgDetectViewModel: ImgDetectionViewModel,
            startingDestination: String) {

    val context = LocalContext.current
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startingDestination
    ) {
        composable("signUpScreen") {

            SignUpScreen(
                navController = navController
            )
        }

        composable("LogInScreen") {

            LogInScreen(
                navController = navController
            )
        }

        composable("home") {

            HomeScreen(navController)
//            AppleHomeUI()
//            HomePage(
//                navController = navController
//            )
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

        composable("websocketOCRscreen") {
//              ImgDescriberScreen()
            websocketORCscreen()
        }

        composable(Route.videoCallingScreen) {
            VideoCallApp()
        }

        composable("videoCallScreen/{userId}/{userName}") {backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?: ""
            val userName = backStackEntry.arguments?.getString("userName")?: ""

//            ZegoCallScreen(userID = userId, userName = userName)
        }
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