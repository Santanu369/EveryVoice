package com.example.everyvoice.TextToSpeech.ttsUi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.everyvoice.MainViewModel
import com.example.everyvoice.TextToSpeech.data.TextData
import kotlinx.coroutines.launch

private val BgBlack = Color(0xFF0B0D12)
private val SurfaceDark = Color(0xFF171A21)
private val SurfaceBorder = Color(0xFF2A2E38)
private val AccentPrimary = Color(0xFF7C8CFF)
private val TextPrimary = Color(0xFFF3F4F8)
private val TextSecondary = Color(0xFF9AA0AE)

@Composable
fun AddTextScreen(viewModel: MainViewModel,
                  navController: NavController) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var title by remember { mutableStateOf(viewModel._title) }
    var text by remember { mutableStateOf(viewModel._text) }

    Scaffold(
        containerColor = BgBlack,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = if (viewModel.isEdit) "Edit Text" else "New Text",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                },
                label = {
                    Text("Title", fontFamily = FontFamily.Monospace)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = AccentPrimary,
                    unfocusedBorderColor = SurfaceBorder,
                    focusedLabelColor = AccentPrimary,
                    unfocusedLabelColor = TextSecondary,
                    cursorColor = AccentPrimary,
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = text,
                onValueChange = {
                    text = it
                },
                label = {
                    Text("Text", fontFamily = FontFamily.Monospace)
                },
                minLines = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 160.dp),
                shape = MaterialTheme.shapes.medium,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark,
                    focusedIndicatorColor = AccentPrimary,
                    unfocusedIndicatorColor = SurfaceBorder,
                    focusedLabelColor = AccentPrimary,
                    unfocusedLabelColor = TextSecondary,
                    cursorColor = AccentPrimary
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {

                Button(
                    onClick = {
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
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentPrimary,
                        contentColor = Color.Black
                    ),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        "Save",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}