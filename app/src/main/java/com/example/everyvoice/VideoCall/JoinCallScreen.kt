package com.example.everyvoice.VideoCall

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun JoinCallScreen(navController: NavController) {

    var userName by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        OutlinedTextField(
            value = userName,
            onValueChange = {
                userName = it
            },
            label = {
                Text("User Name")
            }
        )

        OutlinedTextField(
            value = userId,
            onValueChange = {
                userId = it
            },
            label = {
                Text("User Id")
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.width(280.dp),
            shape = RoundedCornerShape(5.dp),
            onClick = {
                navController.navigate("videoCallScreen/$userId/$userName")
        }) {
            Text("Join Call")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewTest() {
//    JoinCallScreen()
}