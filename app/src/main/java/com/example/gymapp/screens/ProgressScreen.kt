package com.example.gymapp.progress

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.gymapp.screens.AppAccent
import com.example.gymapp.screens.AppAccentMuted
import com.example.gymapp.screens.AppBackground
import com.example.gymapp.screens.AppCardShape
import com.example.gymapp.screens.AppLargeCardShape
import com.example.gymapp.screens.AppPillShape
import com.example.gymapp.screens.AppSurface
import com.example.gymapp.screens.AppSurfaceRaised
import com.example.gymapp.screens.AppSurfaceStroke
import com.example.gymapp.screens.AppTextMuted
import com.example.gymapp.screens.AppTextPrimary
import com.example.gymapp.screens.AppTextSecondary
import com.example.gymapp.screens.MainScaffold
@Composable
fun ProgressScreen(navController: NavController) {
    MainScaffold(
        navController = navController,
        title = "Progress"
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
                                text = "LIFT ANALYTICS",
                                color = AppAccent,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "Choose a lift to inspect your trend line and PR history.",
                            color = AppTextPrimary,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = "Every lift opens a dedicated progress view with recent entries, bodyweight context, and strength level.",
                            color = AppTextSecondary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            item {
                LiftRouteCard(
                    title = "Squat",
                    subtitle = "Lower-body force production and volume capacity.",
                    metric = "Leg Drive",
                    colors = listOf(Color(0xFF151F31), Color(0xFF0F1625)),
                    onClick = { navController.navigate("progress/Squat") }
                )
            }

            item {
                LiftRouteCard(
                    title = "Bench",
                    subtitle = "Upper-body pressing strength and bar speed consistency.",
                    metric = "Pressing",
                    colors = listOf(Color(0xFF191C2F), Color(0xFF101725)),
                    onClick = { navController.navigate("progress/Bench") }
                )
            }

            item {
                LiftRouteCard(
                    title = "Deadlift",
                    subtitle = "Posterior-chain output and heavy pull progression.",
                    metric = "Pulling",
                    colors = listOf(Color(0xFF172230), Color(0xFF101725)),
                    onClick = { navController.navigate("progress/Deadlift") }
                )
            }
        }
    }
}

@Composable
private fun LiftRouteCard(
    title: String,
    subtitle: String,
    metric: String,
    colors: List<Color>,
    onClick: () -> Unit
) {
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
                .background(Brush.linearGradient(colors))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = title,
                        color = AppTextPrimary,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = subtitle,
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
                        text = metric,
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
                ProgressPill(modifier = Modifier.weight(1f), label = "Trend", value = "Open")
                ProgressPill(modifier = Modifier.weight(1f), label = "View", value = "Detailed")
                Box(
                    modifier = Modifier
                        .clip(AppCardShape)
                        .background(AppSurfaceRaised)
                        .border(1.dp, AppSurfaceStroke, AppCardShape)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ">",
                        color = AppTextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressPill(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Column(
        modifier = modifier
            .clip(AppCardShape)
            .background(AppSurfaceRaised)
            .border(1.dp, AppSurfaceStroke, AppCardShape)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
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

