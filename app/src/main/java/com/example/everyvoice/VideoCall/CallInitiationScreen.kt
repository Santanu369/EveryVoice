package com.example.everyvoice.VideoCall

// CallInitiationScreen.kt
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CallInitiationScreen(
    onStartCall: (String) -> Unit
) {
    Log.d("ws", "callinitiation")
    var recipientName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Start Video Call",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = recipientName,
            onValueChange = { recipientName = it },
            label = { Text("Enter recipient name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (recipientName.isNotEmpty()) {
                    onStartCall(recipientName)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Call")
        }
    }
}