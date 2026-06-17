package com.example.everyvoice.TextToSpeech.ttsUi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.everyvoice.MainViewModel
import com.example.everyvoice.TextToSpeech.data.TextData
import kotlinx.coroutines.launch

@Composable
fun AddTextScreen(viewModel: MainViewModel,
                  navController: NavController) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var title by remember { mutableStateOf(viewModel._title) }
    var text by remember { mutableStateOf(viewModel._text) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
                .padding(50.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                },
                label = {
                    Text("Title")
                }
            )

            Spacer(modifier = Modifier.height(50.dp))

            TextField(
                value = text,
                onValueChange = {
                    text = it
                },
                label = {
                    Text("Text")
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {

                Button(onClick = {
                    scope.launch {
                        if(viewModel.isEdit == false) {
                            viewModel.upsertText(
                                TextData(
                                    title = title,
                                    text = text
                                )
                            )
                        }
                        else {
                            viewModel.upsertText(
                                TextData(
                                    id = viewModel._id,
                                    title = title,
                                    text = text
                                )
                            )
                            viewModel.isEdit = false
                            viewModel._title = ""
                            viewModel._text = ""
                        }
                        navController.popBackStack()
                        snackbarHostState.showSnackbar("Text added!")
                    }
                }) {
                    Text("Save")
                }
            }


        }
    }
}