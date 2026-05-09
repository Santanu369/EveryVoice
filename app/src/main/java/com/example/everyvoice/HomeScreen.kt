package com.example.everyvoice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.everyvoice.Utils.rememberTTS

@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()
    ) {
        val tts = rememberTTS()

        Image(painterResource(R.drawable.bg_image2),
            contentDescription = "BackGround Image",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(40.dp))

            FeatureCard(text = "Text To Speech",
                onclick = {
                    navController.navigate("textToSpeech")
                })


            FeatureCard(text = "Emergency Features",
                modifier = Modifier.clickable {
//                    navController.navigate("emergencyScreen")
                })

            Spacer(modifier = Modifier.height(50.dp))

            Row(modifier = Modifier
                .horizontalScroll(rememberScrollState())) {

                FeatureCard(
                    text = "Image Recognizer",
                    onclick = {
                        tts.speak("Image Recognizer")
                    },
                    onLongClick = {
                        navController.navigate("imgLabelingScreen")
                    },
//                    height = 500.dp
                    height = 300.dp
                )

                FeatureCard(
                    text = "Text Recognizer",
                    onclick = {
                        tts.speak("Text Recognizer")
                    },
                    onLongClick = {
                        navController.navigate("textRecognizerScreen")
                    },
//                    height = 500.dp
                    height = 300.dp
                )

                FeatureCard(
                    text = "Sign Language TTS",
                    onclick = {
                        tts.speak("Sign Language Text To Speech")
                    },
                    onLongClick = {
                        navController.navigate("signLangTTS")
                    },
//                    height = 500.dp
                    height = 300.dp
                )

                FeatureCard(
                    text = "Voice To text",
                    onclick = {
                        tts.speak("Voice To text")
                    },
                    onLongClick = {
                        navController.navigate("speechToText")
                    },
//                    height = 500.dp
                    height = 300.dp
                )
            }

        }

    }
}

@Composable
fun FeatureCard(
    modifier: Modifier = Modifier,
    text: String,
    onclick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    height: Dp = 100.dp
) {

    GlassMorphism(
        modifier = Modifier
            .padding(top = 16.dp, start = 32.dp, end = 32.dp)
            .width(320.dp)
            .height(height)
            .clickable { onclick() }
            .combinedClickable(
                onLongClick = { onLongClick() },
                onClick = { onclick() },
            ),
        blurRadius = 32.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    text = "Beyoncé (born 1981) is an American singer, songwriter, actress, and businesswoman. She rose to fame in the late 1990s as the lead vocalist of Destiny's Child, one of the best-selling girl groups.",
//                    fontSize = 16.sp,
//                    color = Color.White.copy(alpha = 0.7f),
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.fillMaxSize()
//                )
        }
    }

}

@Composable
fun GlassMorphism(
    modifier: Modifier = Modifier,
    blurRadius: Dp =  16.dp,
    backgroundColor: Color = Color.White.copy(alpha = 0.15f),
    cornerRadius: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(cornerRadius)
            )
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(cornerRadius))
                .background(backgroundColor)
                .blur(radius = blurRadius)
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(16.dp)
        ) {
            content()
        }
    }
}