package com.example.gymapp.screens

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gymapp.viewmodel.UserUiState
import com.example.gymapp.viewmodel.UserViewModel

@Composable
fun StartScreen(
    navController: NavController,
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val uiState by userViewModel.uiState.collectAsState()

    when (uiState) {

        is UserUiState.Loading -> {
            // Dok čekamo bazu – ne navigiramo nigde
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UserUiState.NoUser -> {
            LaunchedEffect(Unit) {
                navController.navigate("register") {
                    popUpTo("start") { inclusive = true }
                }
            }
        }

        is UserUiState.HasUser -> {
            LaunchedEffect(Unit) {
                navController.navigate("home") {
                    popUpTo("start") { inclusive = true }
                }
            }
        }
    }
}
