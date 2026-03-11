package com.example.gymapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymapp.data.Program
import com.example.gymapp.data.ProgramTemplates

@Composable
fun ProgramsScreen(navController: NavController) {
    val programs = ProgramTemplates.fiveByFive() +
        ProgramTemplates.fiveThreeOne() +
        ProgramTemplates.hybrid()

    MainScaffold(
        navController = navController,
        title = "Programs"
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = AppLargeCardShape,
                    colors = CardDefaults.cardColors(containerColor = AppSurface),
                    border = BorderStroke(1.dp, AppSurfaceStroke),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.verticalGradient(listOf(AppSurfaceRaised, AppSurface)))
                            .padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(AppPillShape)
                                .background(AppAccentMuted)
                                .border(1.dp, AppSurfaceStroke, AppPillShape)
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = "PROGRAM LIBRARY",
                                color = AppAccent,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "Choose a structured plan built around strength progression.",
                            color = AppTextPrimary,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = "Each program card focuses on format, duration, and training intent. Existing decorative images are intentionally replaced with a cleaner premium treatment.",
                            color = AppTextSecondary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            items(programs) { program ->
                ProgramListCard(
                    program = program,
                    onClick = { navController.navigate("program/${program.id}") }
                )
            }
        }
    }
}

@Composable
private fun ProgramListCard(
    program: Program,
    onClick: () -> Unit
) {
    val accentColors = when (program.id) {
        "five_by_five" -> listOf(Color(0xFF162030), Color(0xFF0F1625))
        "five_three_one" -> listOf(Color(0xFF1A1F32), Color(0xFF101725))
        "Hybrid_Strength" -> listOf(Color(0xFF152432), Color(0xFF101725))
        else -> listOf(AppSurfaceRaised, AppSurface)
    }

    val duration = when (program.id) {
        "five_by_five" -> "16 workouts"
        "five_three_one" -> "16 weeks"
        else -> "4 weeks"
    }

    val focus = when (program.id) {
        "five_by_five" -> "Linear base building"
        "five_three_one" -> "Periodized power cycle"
        "Hybrid_Strength" -> "Top sets plus volume"
        else -> "Strength"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = AppLargeCardShape,
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        border = BorderStroke(1.dp, AppSurfaceStroke),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(accentColors))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = program.name,
                        color = AppTextPrimary,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = focus,
                        color = AppAccent,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = program.description.lineSequence().firstOrNull().orEmpty(),
                        color = AppTextSecondary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(AppPillShape)
                        .background(AppAccentMuted)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = duration,
                        color = AppAccent,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProgramMeta(modifier = Modifier.weight(1f), label = "Exercises", value = program.exercises.size.toString())
                ProgramMeta(modifier = Modifier.weight(1f), label = "Structure", value = programStructure(program))
                ProgramMeta(modifier = Modifier.weight(1f), label = "Focus", value = "Strength")
            }
        }
    }
}

@Composable
private fun ProgramMeta(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Column(
        modifier = modifier
            .clip(AppCardShape)
            .background(AppSurfaceRaised)
            .border(1.dp, AppSurfaceStroke, AppCardShape)
            .padding(horizontal = 14.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label.uppercase(),
            color = AppTextMuted,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            color = AppTextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun programStructure(program: Program): String {
    return when (program.id) {
        "five_by_five" -> "5x5"
        "five_three_one" -> "5/3/1"
        "Hybrid_Strength" -> "Hybrid"
        else -> "Plan"
    }
}


