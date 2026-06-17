package com.example.everyvoice.TextToSpeech.ttsUi

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.everyvoice.MainViewModel
import com.example.everyvoice.TextToSpeech.data.TextData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextToSpeechScreen(viewModel: MainViewModel, navController: NavController) {

    val softGreen = Color(0xFF99FF99)


    val allTextList by viewModel.allText.collectAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Text To Speech")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF99FF99),
                    titleContentColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("addTextScreen")
                },
                containerColor = softGreen,
                contentColor = Color.Black,
                modifier = Modifier.size(70.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) {

        LazyColumn(modifier = Modifier
            .background(Color.Black)
            .padding(it)
            .fillMaxSize()) {

            items(allTextList) {
                SwipeToDismissText(it, {
                    viewModel._id = it.id
                    viewModel._title = it.title
                    viewModel._text = it.text
                    viewModel.isEdit = true
                    navController.navigate("addTextScreen")
                },
                    onSelect = {
                        val title = it.title
                        val text = it.text
                        navController.navigate("textToSpeechGen/${title}/${text}")
                    },
                    onDelete = {
                        viewModel.deleteText(it)
                    },
                    )
            }

        }
    }
}

@Composable
fun TextBox(textData: TextData,
            onEdit: () -> Unit,
            onSelect: () -> Unit) {

    val softGreen = Color(0xFF99FF99)

    Row (modifier = Modifier.padding(top=32.dp, start=32.dp, end=32.dp)
        .background(Color.Black, shape = RoundedCornerShape(6.dp))
        .border(width = 1.dp, color = softGreen, shape = RoundedCornerShape(16.dp))
        .fillMaxWidth()
        .height(70.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {

        Text(text = textData.title, fontSize = 18.sp, color = Color.White, fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(8.dp)
                .width(250.dp)
                .clickable {
                    onSelect()
                })

        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Edit, contentDescription = "edit text",
                modifier = Modifier.size(30.dp)
                    .clickable {
                        onEdit()
                    },
                tint = Color.White)
        }
    }

}

@Composable
fun SwipeToDismissText(textData: TextData,
                       onEdit: () -> Unit,
                       onSelect: () -> Unit,
                       onDelete: () -> Unit) {

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (
                value == SwipeToDismissBoxValue.StartToEnd ||
                value == SwipeToDismissBoxValue.EndToStart
            ) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            // optional background while swiping
        }
    ) {
        TextBox(
            textData = textData,
            onEdit = onEdit,
            onSelect = onSelect
        )
    }
}