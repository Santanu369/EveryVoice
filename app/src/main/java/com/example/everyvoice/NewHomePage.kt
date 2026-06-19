package com.example.everyvoice

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.everyvoice.ImageRecognizer.ImageDetectionGemini
import com.example.everyvoice.ImageRecognizer.ImgDetectionViewModel

// ─── Color tokens ─────────────────────────────────────────────────────────────

private object C {
    val bg         = Color(0xFFF2F2F7)
    val card       = Color(0xFFFFFFFF)
    val accent     = Color(0xFF007AFF)
    val label      = Color(0xFF1C1C1E)
    val secondary  = Color(0xFF8E8E93)
    val tertiary   = Color(0xFFC7C7CC)
    val fill       = Color(0xFFE5E5EA)
    val divider    = Color(0xFFC6C6C8)
}

// ─── Nav model ────────────────────────────────────────────────────────────────

private data class NavItem(
    val label: String,
    val selected: ImageVector,
    val unselected: ImageVector,
)

private val NAV_ITEMS = listOf(
    NavItem("Home",    Icons.Filled.Home,         Icons.Outlined.Home),
    NavItem("Search",  Icons.Filled.Search,       Icons.Outlined.Search),
    NavItem("My AI", Icons.Filled.AddAPhoto, Icons.Outlined.AddAPhoto),
    NavItem("Explore", Icons.Filled.Explore,      Icons.Outlined.Explore),
    NavItem("Profile", Icons.Filled.Settings,       Icons.Outlined.Settings),
)

// ─── Root composable ──────────────────────────────────────────────────────────

@Composable
fun AppleHomeUI() {
    val ImgViewModel: ImgDetectionViewModel = viewModel()

    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = C.bg,
        bottomBar = {
            BottomBar(
                items = NAV_ITEMS,
                selectedIndex = selectedTab,
                onSelect = { selectedTab = it },
            )
        },
    ) { padding ->
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                (fadeIn(tween(180)) + slideInVertically { it / 28 })
                    .togetherWith(fadeOut(tween(130)) + slideOutVertically { -it / 28 })
            },
            label = "tab",
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) { tab ->
            when (tab) {
                0 -> HomeTab()
                1 -> PlaceholderTab("Search",  Icons.Outlined.Search,       "Find anything instantly")
                2 -> ImageDetectionGemini(ImgViewModel)
                3 -> PlaceholderTab("Explore", Icons.Outlined.Explore,      "Discover what's new")
                4 -> PlaceholderTab("Profile", Icons.Outlined.Person,       "Your account & settings")
            }
        }
    }
}

// ─── Bottom bar ───────────────────────────────────────────────────────────────

@Composable
private fun BottomBar(
    items: List<NavItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
) {
    Surface(
        color = C.card.copy(alpha = 0.92f),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            HorizontalDivider(color = C.divider.copy(alpha = 0.45f), thickness = 0.5.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .height(58.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEachIndexed { i, item ->
                    NavPill(
                        item = item,
                        selected = selectedIndex == i,
                        onClick = { onSelect(i) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun NavPill(
    item: NavItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1f else 0.90f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "scale",
    )
    val alpha by animateFloatAsState(
        targetValue = if (selected) 1f else 0.42f,
        animationSpec = tween(180),
        label = "alpha",
    )

    Column(
        modifier = modifier
            .scale(scale)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = if (selected) item.selected else item.unselected,
            contentDescription = item.label,
            tint = if (selected) C.accent else C.secondary.copy(alpha = alpha),
            modifier = Modifier.size(24.dp),
        )
        Spacer(Modifier.height(3.dp))
        Text(
            text = item.label,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (selected) C.accent else C.secondary.copy(alpha = alpha),
            letterSpacing = (-0.1).sp,
        )
    }
}

// ─── Home tab ─────────────────────────────────────────────────────────────────

@Composable
private fun HomeTab() {
    val chips = listOf("For You", "New", "Trending", "Saved", "Recent")
    var activeChip by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(C.bg),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        // Large title
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 52.dp, bottom = 6.dp),
            ) {
                Text("Good morning,", fontSize = 15.sp, color = C.secondary)
                Text(
                    "Alex",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = C.label,
                    letterSpacing = (-0.5).sp,
                )
            }
        }

        // Chips
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 22.dp),
            ) {
                items(chips.size) { i ->
                    val sel = i == activeChip
                    Surface(
                        shape = CircleShape,
                        color = if (sel) C.accent else C.fill,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ) { activeChip = i },
                    ) {
                        Text(
                            text = chips[i],
                            fontSize = 14.sp,
                            fontWeight = if (sel) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (sel) Color.White else C.label,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp),
                        )
                    }
                }
            }
        }

        // Featured card
        item {
            FeaturedCard(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 28.dp),
            )
        }

        // Section: Continue
        item {
            SectionHeader(
                "Continue Reading", "See All",
                Modifier.padding(horizontal = 20.dp).padding(bottom = 14.dp),
            )
        }

        item {
            val smallCards = listOf(
                Triple("Typography in Motion", "Design", Color(0xFF0A84FF)),
                Triple("Color Theory",         "Art",    Color(0xFF30D158)),
                Triple("Grid Systems",          "Layout", Color(0xFFFF9F0A)),
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 28.dp),
            ) {
                items(smallCards) { (title, tag, color) ->
                    SmallCard(title, tag, color)
                }
            }
        }

        // Section: Recommended
        item {
            SectionHeader(
                "Recommended", "See All",
                Modifier.padding(horizontal = 20.dp).padding(bottom = 14.dp),
            )
        }

        items(
            listOf(
                "Understanding Contrast Ratios",
                "Spacing & Rhythm",
                "Motion Design Principles",
            )
        ) { title ->
            ListCard(
                title = title,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 10.dp),
            )
        }
    }
}

// ─── Card components ──────────────────────────────────────────────────────────

@Composable
private fun FeaturedCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(listOf(Color(0xFF1C1C3A), Color(0xFF3A1C5A)))
            ),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color.White.copy(alpha = 0.18f),
            ) {
                Text(
                    "FEATURED",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                "Design Systems",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = (-0.3).sp,
            )
            Text(
                "How great apps stay consistent",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.72f),
            )
        }
    }
}

@Composable
private fun SmallCard(title: String, tag: String, accent: Color) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = C.card,
        modifier = Modifier.size(160.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accent.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(accent),
                )
            }
            Column {
                Text(tag.uppercase(), fontSize = 10.sp, color = accent, fontWeight = FontWeight.SemiBold, letterSpacing = 0.6.sp)
                Spacer(Modifier.height(3.dp))
                Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = C.label, letterSpacing = (-0.2).sp)
            }
        }
    }
}

@Composable
private fun ListCard(title: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = C.card,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = C.label, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = C.tertiary, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String, action: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = C.label, letterSpacing = (-0.3).sp)
        Text(action, fontSize = 15.sp, color = C.accent)
    }
}

// ─── Placeholder screens ──────────────────────────────────────────────────────

@Composable
private fun PlaceholderTab(title: String, icon: ImageVector, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(C.bg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(C.accent.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = C.accent, modifier = Modifier.size(38.dp))
        }
        Spacer(Modifier.height(20.dp))
        Text(title, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = C.label, letterSpacing = (-0.4).sp)
        Spacer(Modifier.height(6.dp))
        Text(subtitle, fontSize = 15.sp, color = C.secondary, textAlign = TextAlign.Center)
    }
}

@Preview
@Composable
fun Preview() {
    AppleHomeUI()
}