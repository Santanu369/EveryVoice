package com.example.everyvoice.TextToSpeech.ttsUi

import androidx.compose.foundation.background
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
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

@Composable
fun TextToSpeechScreen(viewModel: MainViewModel, navController: NavController) {


    val allTextList by viewModel.allText.collectAsState(emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("addTextScreen")
                }
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
                    })
            }

        }
    }
}

@Composable
fun TextBox(textData: TextData,
            onEdit: () -> Unit,
            onSelect: () -> Unit) {


    Row (modifier = Modifier.padding(top=32.dp, start=32.dp, end=32.dp)
        .background(Color.White, shape = RoundedCornerShape(3.dp))
        .fillMaxWidth()
        .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {

        Text(text = textData.title, fontSize = 16.sp, color = Color.Black, fontFamily = FontFamily.Monospace,
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
                tint = Color.Black)
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