package com.example.gymapp.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gymapp.viewmodel.UserUiState
import com.example.gymapp.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController
) {
    val userViewModel: UserViewModel = hiltViewModel()
    // State promenljive za unos
    var name by remember { mutableStateOf("") }
    var squat by remember { mutableStateOf("") }
    var bench by remember { mutableStateOf("") }
    var deadlift by remember { mutableStateOf("") }

    val userState by userViewModel.uiState.collectAsState()
    LaunchedEffect(userState) {
        if (userState is UserUiState.HasUser) {
            navController.navigate("home") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Register") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Ime korisnika
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Squat 1RM
            OutlinedTextField(
                value = squat,
                onValueChange = { squat = it.filter { c -> c.isDigit() } },
                label = { Text("Squat 1RM (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Bench 1RM
            OutlinedTextField(
                value = bench,
                onValueChange = { bench = it.filter { c -> c.isDigit() } },
                label = { Text("Bench 1RM (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Deadlift 1RM
            OutlinedTextField(
                value = deadlift,
                onValueChange = { deadlift = it.filter { c -> c.isDigit() } },
                label = { Text("Deadlift 1RM (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Dugme za registraciju
            Button(
                onClick = {
                    if (name.isNotBlank() && squat.isNotBlank() && bench.isNotBlank() && deadlift.isNotBlank()) {
                        userViewModel.registerUser(
                            name,
                            squat.toInt(),
                            bench.toInt(),
                            deadlift.toInt()
                        )
//                        navController.navigate("home") {
//                            popUpTo("register") { inclusive = true }
//                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Training")
            }
        }
    }
}
