package com.example.everyvoice

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

data class CardItem(
    val title: String,
    val desc: String,
    val icon: ImageVector,
    val iconBg: Color,
    val iconTint: Color,
    val route: String
)

@Composable
fun HomeScreen(navController: NavController) {

    val Bg = Color(0xFF0A0A0A)
    val Surface = Color(0xFF1C1C1E)
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFF8E8E93)

    val cards = listOf(
        CardItem("Sign Language TTS",  "sign language tts from external device",   Icons.Default.Bookmark,     Color(0xFF1E1A3A), Color(0xFF7B6FCC), "signLangTTS"),
        CardItem("Voice To Text",       "convert voice to text",      Icons.Default.History,      Color(0xFF0F2A22), Color(0xFF2EAF82), "speechToText"),
        CardItem("Text To speech",        "convert text to speech",         Icons.Default.Tune,         Color(0xFF2A1F0A), Color(0xFFCC8A2E),
            "textToSpeech"),
        CardItem("OCR","text recognizer",        Icons.Default.CenterFocusStrong,Color(0xFF2A140A), Color(0xFFCC5A2E), "textRecognizerScreen")
    )

    Scaffold(containerColor = Bg) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // Header
            item {
                Column(Modifier.fillMaxWidth().padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Row {
                        Text("Every", fontSize = 30.sp, fontWeight = FontWeight.Bold,
                            color = TextPrimary, letterSpacing = (-0.8).sp)
                        Text("Voice", fontSize = 30.sp, fontWeight = FontWeight.Bold,
                            color = Color(0xFF378ADD), letterSpacing = (-0.8).sp)
                    }
                    Text("Your words, heard by the world",
                        fontSize = 13.sp, color = TextSecondary, modifier = Modifier.padding(top = 4.dp))
                }
            }

            // Hero card
            item {
                Column(
                    Modifier.fillMaxWidth()
                        .background(Surface, RoundedCornerShape(16.dp))
                        .clickable { navController.navigate("imgLabelingScreen") }
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(Modifier.size(60.dp).background(Color(0xFF1A2F4A), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.PhotoCamera, null, Modifier.size(28.dp), tint = Color(0xFF4A8DFF))
                    }
                    Spacer(Modifier.height(10.dp))
                    Text("MY AI", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Text("AI image description",
                        fontSize = 13.sp, color = TextSecondary, modifier = Modifier.padding(top = 3.dp))
                }
            }

            // Full-width card
            item { NavCard(cards[0], Surface, TextPrimary, TextSecondary, navController) }

            // 2-column row
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SmallCard(cards[1], Surface, TextPrimary, TextSecondary, navController, Modifier.weight(1f))
                    SmallCard(cards[2], Surface, TextPrimary, TextSecondary, navController, Modifier.weight(1f))
                }
            }

            // Full-width card
            item { NavCard(cards[3], Surface, TextPrimary, TextSecondary, navController) }
        }
    }
}

@Composable
fun NavCard(item: CardItem, surface: Color, tp: Color, ts: Color, nav: NavController) {
    Row(
        Modifier.fillMaxWidth().background(surface, RoundedCornerShape(16.dp))
            .clickable { nav.navigate(item.route) }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(Modifier.size(50.dp).background(item.iconBg, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center) {
            Icon(item.icon, null, Modifier.size(24.dp), tint = item.iconTint)
        }
        Column(Modifier.weight(1f)) {
            Text(item.title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = tp)
            Text(item.desc, fontSize = 12.sp, color = ts)
        }
        Icon(Icons.Default.ChevronRight, null, Modifier.size(18.dp), tint = ts)
    }
}

@Composable
fun SmallCard(item: CardItem, surface: Color, tp: Color, ts: Color, nav: NavController, modifier: Modifier) {
    Column(
        modifier.background(surface, RoundedCornerShape(16.dp))
            .clickable { nav.navigate(item.route) }.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(Modifier.size(44.dp).background(item.iconBg, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center) {
            Icon(item.icon, null, Modifier.size(20.dp), tint = item.iconTint)
        }
        Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = tp)
        Text(item.desc, fontSize = 11.sp, color = ts)
    }
}

@Preview
@Composable
fun HomePreview() {
    HomeScreen(rememberNavController())
}