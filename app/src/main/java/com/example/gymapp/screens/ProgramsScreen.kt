package com.example.gymapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymapp.R
import com.example.gymapp.data.ProgramTemplates

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramsScreen(navController: NavController) {

    // Ako i dalje koristiš template objekte zbog id/name:
    val programs = ProgramTemplates.fiveByFive() +
            ProgramTemplates.fiveThreeOne() +
            ProgramTemplates.hybrid()

    // mapiranje program.id -> slika
    val programImageRes: (String) -> Int = { id ->
        when (id) {
            "five_by_five" -> R.drawable.fivebyfive
            "five_three_one" -> R.drawable.fivethreeone
            "Hybrid_Strength" -> R.drawable.hybrid
            else -> R.drawable.hybrid // fallback (stavi šta hoćeš)
        }
    }

    MainScaffold(
        navController = navController,
        title = "Programs"
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFF07162e)),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(programs) { program ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clickable { navController.navigate("program/${program.id}") },
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1c2d4a)),
                    border = BorderStroke(2.dp, Color(0xFF491669))
                ) {
                    Image(
                        painter = painterResource(id = programImageRes(program.id)),
                        contentDescription = program.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

