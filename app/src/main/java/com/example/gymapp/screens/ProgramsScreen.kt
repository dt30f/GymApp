package com.example.gymapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymapp.data.ProgramTemplates

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramsScreen(navController: NavController) {
    val programs = ProgramTemplates.fiveByFive() +
            ProgramTemplates.fiveThreeOne() +
            ProgramTemplates.hybrid()

    MainScaffold(
        navController = navController,
        title = "Programs"
    ){ padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFF07162e)), // tamna pozadina
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(programs) { program ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("program/${program.id}")
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1c2d4a) // tamnija plava za Card
                    ),
                    border = BorderStroke(2.dp, Color(0xFF491669))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = program.name,
                            style = MaterialTheme.typography.titleLarge.copy(color = Color.Red)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = Color(0xFF491669).copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Exercises: ${program.exercises.joinToString(", ") { it.exercise }}",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Yellow.copy(alpha = 0.85f))
                        )
                    }
                }
            }
        }
    }

}

