package com.example.gymapp.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController

private val Background = Color(0xFF0B1220)
private val TopBarBg = Color(0xFF070E1B)
private val Surface = Color(0xFF111A2E)
private val Surface2 = Color(0xFF141F36)
private val Accent = Color(0xFFE53935)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFAAB3C5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavController,
    title: String,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(
                    title,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                ) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = TopBarBg
                ),
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate("profile")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = content
    )
}
