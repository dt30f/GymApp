package com.example.gymapp.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavController,
    title: String,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFF07162e),
        topBar = {
            TopAppBar(
                title = { Text(
                    title,
                    color = Color.Red
                ) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF070e1b), // isto i za TopAppBar
                    titleContentColor = Color.White
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
