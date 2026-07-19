package com.example.everyvoice.TextToSpeech.ttsUi

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.everyvoice.MainViewModel
import com.example.everyvoice.TextToSpeech.data.TextData

private val BgBlack = Color(0xFF0B0D12)
private val SurfaceDark = Color(0xFF171A21)
private val SurfaceBorder = Color(0xFF2A2E38)
private val AccentPrimary = Color(0xFF7C8CFF)
private val AccentPrimaryDim = Color(0xFF4E58A8)
private val DangerRed = Color(0xFFFF6B6B)
private val TextPrimary = Color(0xFFF3F4F8)
private val TextSecondary = Color(0xFF9AA0AE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextToSpeechScreen(viewModel: MainViewModel, navController: NavController) {

    val allTextList by viewModel.allText.collectAsState(emptyList())

    Scaffold(
        containerColor = BgBlack,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Text To Speech",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceDark,
                    titleContentColor = TextPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("addTextScreen")
                },
                containerColor = AccentPrimary,
                contentColor = Color.Black,
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add new text")
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgBlack)
                .padding(padding)
        ) {
            if (allTextList.isEmpty()) {
                Text(
                    text = "No entries yet.\nTap + to add your first text.",
                    color = TextSecondary,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 15.sp,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    top = 16.dp,
                    bottom = 96.dp
                )
            ) {
                items(allTextList, key = { it.id }) {
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
}

@Composable
fun TextBox(
    textData: TextData,
    onEdit: () -> Unit,
    onSelect: () -> Unit
) {

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(76.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(SurfaceDark)
            .border(width = 1.dp, color = SurfaceBorder, shape = RoundedCornerShape(18.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .clickable { onSelect() }
                .padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(AccentPrimaryDim),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = AccentPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = textData.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = textData.text,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(end = 14.dp)
                .size(38.dp)
                .clip(CircleShape)
                .background(SurfaceBorder)
                .clickable { onEdit() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "edit text",
                modifier = Modifier.size(18.dp),
                tint = TextPrimary
            )
        }
    }
}

@Composable
fun SwipeToDismissText(
    textData: TextData,
    onEdit: () -> Unit,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {

    androidx.compose.runtime.key(textData.id) {

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
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .height(76.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(DangerRed.copy(alpha = 0.85f))
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "delete",
                        tint = Color.White
                    )
                }
            }
        ) {
            TextBox(
                textData = textData,
                onEdit = onEdit,
                onSelect = onSelect
            )
        }
    }
}
