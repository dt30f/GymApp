package com.example.gymapp.progress

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.gymapp.R

private val Background = Color(0xFF0B1220)
private val TopBarBg = Color(0xFF070E1B)
private val Surface = Color(0xFF111A2E)
private val Surface2 = Color(0xFF141F36)
private val Accent = Color(0xFFE53935)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFAAB3C5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(navController: NavController) {

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Your Progress",
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Track progress on your lifts",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = TopBarBg
                ),
            )

        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LiftImageButton(
                imageRes = R.drawable.squat,
                onClick = { navController.navigate("progress/Squat") }
            )

            LiftImageButton(
                imageRes = R.drawable.bench,
                onClick = { navController.navigate("progress/Bench") }
            )

            LiftImageButton(
                imageRes = R.drawable.deadlift,
                onClick = { navController.navigate("progress/Deadlift") }
            )
        }
    }
}

@Composable
fun LiftImageButton(imageRes: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.6f) // automatski prilagođava visinu slici
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop, // popunjava ceo Card
            modifier = Modifier.fillMaxSize()
        )
    }
}
