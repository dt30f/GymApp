package com.example.gymapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gymapp.viewmodel.UserUiState
import com.example.gymapp.viewmodel.UserViewModel

@Composable
fun UserProfileScreen(
    navController: NavController
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val uiState by userViewModel.uiState.collectAsState()

    when (uiState) {

        is UserUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UserUiState.NoUser -> {
            // Ako nekako nema usera, vrati ga na register
            LaunchedEffect(Unit) {
                navController.navigate("register") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }

        is UserUiState.HasUser -> {

            val user = (uiState as UserUiState.HasUser).user

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.headlineMedium
                )

                HorizontalDivider()

                Text("Name: ${user.name}")
                Text("Squat 1RM: ${user.squat1RM} kg")
                Text("Bench 1RM: ${user.bench1RM} kg")
                Text("Deadlift 1RM: ${user.deadlift1RM} kg")

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        // Logout = obriši user iz baze
                        userViewModel.logout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Logout")
                }
            }
        }
    }
}
