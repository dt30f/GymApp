package com.example.gymapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

val AppBackground = Color(0xFF070B14)
val AppTopBar = Color(0xFF0B111D)
val AppSurface = Color(0xFF101725)
val AppSurfaceRaised = Color(0xFF141D2E)
val AppSurfaceStroke = Color(0xFF1E2940)
val AppAccent = Color(0xFFFF5A36)
val AppAccentMuted = Color(0x26FF5A36)
val AppSuccess = Color(0xFF3DDC97)
val AppTextPrimary = Color(0xFFF4F7FB)
val AppTextSecondary = Color(0xFF98A4B8)
val AppTextMuted = Color(0xFF657188)
val AppLargeCardShape = RoundedCornerShape(28.dp)
val AppCardShape = RoundedCornerShape(22.dp)
val AppPillShape = RoundedCornerShape(999.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavController,
    title: String,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        color = AppTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTopBar,
                    titleContentColor = AppTextPrimary
                ),
                actions = {
                    Surface(
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(1.dp, AppSurfaceStroke, CircleShape),
                        color = AppSurfaceRaised,
                        shape = CircleShape
                    ) {
                        Row {
                            IconButton(onClick = { navController.navigate("profile") }) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Profile",
                                    tint = AppTextPrimary,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(Color.Transparent)
                                )
                            }
                        }
                    }
                }
            )
        },
        content = content
    )
}
