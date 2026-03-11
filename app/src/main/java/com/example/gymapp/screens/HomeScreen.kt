package com.example.gymapp.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

@Composable
fun HomeScreen(navController: NavController) {
    MainScaffold(
        navController = navController,
        title = "Gym Tracker"
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
                DashboardHero()
            }

            item {
                NavigationCard(
                    eyebrow = "ANALYTICS",
                    title = "Track Lift Progress",
                    description = "Review performance trends, PRs, and bodyweight-relative strength across your main lifts.",
                    metricLabel = "Focus",
                    metricValue = "Progress",
                    accentColors = listOf(AppSurfaceRaised, AppSurface),
                    onClick = { navController.navigate("progress") }
                )
            }

            item {
                NavigationCard(
                    eyebrow = "PROGRAMS",
                    title = "Explore Strength Plans",
                    description = "Open structured templates, inspect progression details, and generate your next training cycle.",
                    metricLabel = "Focus",
                    metricValue = "Planning",
                    accentColors = listOf(AppSurface, AppSurfaceRaised),
                    onClick = { navController.navigate("programs") }
                )
            }

            item {
                NavigationCard(
                    eyebrow = "ACTIVE",
                    title = "Active Programs",
                    description = "Manage programs you already started, organized by lift and backed by persisted plans.",
                    metricLabel = "Focus",
                    metricValue = "Management",
                    accentColors = listOf(Color(0xFF162534), Color(0xFF101725)),
                    onClick = { navController.navigate("active_programs") }
                )
            }
        }
    }
}

@Composable
private fun DashboardHero() {
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
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(AppSurfaceRaised, AppSurface)
                    )
                )
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(AppPillShape)
                    .background(AppAccentMuted)
                    .border(1.dp, AppSurfaceStroke, AppPillShape)
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(
                    text = "TRAIN SMART",
                    color = AppAccent,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Build a clean, disciplined training workflow.",
                color = AppTextPrimary,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "Your dashboard now connects lift analytics, program planning, and active program management in one place.",
                color = AppTextSecondary,
                style = MaterialTheme.typography.bodyLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickStat(modifier = Modifier.weight(1f), label = "Modules", value = "3")
                QuickStat(modifier = Modifier.weight(1f), label = "Theme", value = "Dark")
                QuickStat(modifier = Modifier.weight(1f), label = "Goal", value = "Strength")
            }
        }
    }
}

@Composable
private fun QuickStat(
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

@Composable
private fun NavigationCard(
    eyebrow: String,
    title: String,
    description: String,
    metricLabel: String,
    metricValue: String,
    accentColors: List<Color>,
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
                .background(Brush.linearGradient(accentColors))
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
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = eyebrow,
                        color = AppAccent,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = title,
                        color = AppTextPrimary,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = description,
                        color = AppTextSecondary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(AppAccentMuted)
                        .size(44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ">",
                        color = AppAccent,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = metricLabel.uppercase(),
                        color = AppTextMuted,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = metricValue,
                        color = AppTextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onClick,
                    shape = AppPillShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppAccent,
                        contentColor = AppTextPrimary
                    )
                ) {
                    Text("Open", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

