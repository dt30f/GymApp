package com.example.gymapp.screens.programs

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gymapp.data.ActiveProgram
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
import com.example.gymapp.viewmodel.ActiveProgramsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActiveProgramsScreen(
    navController: NavController,
    viewModel: ActiveProgramsViewModel = hiltViewModel()
) {
    val activePrograms by viewModel.activePrograms.collectAsState()

    MainScaffold(
        navController = navController,
        title = "Active Programs"
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
                HeaderCard(activeCount = activePrograms.size)
            }

            if (activePrograms.isEmpty()) {
                item {
                    EmptyStateCard()
                }
            } else {
                items(activePrograms) { program ->
                    ActiveProgramCard(
                        program = program,
                        onClick = { navController.navigate("active_program/${program.id}") }
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderCard(activeCount: Int) {
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
                    text = "CURRENT CYCLES",
                    color = AppAccent,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Manage your active programs by lift.",
                color = AppTextPrimary,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "Only one active cycle is allowed per lift, so this list is your single source of truth.",
                color = AppTextSecondary,
                style = MaterialTheme.typography.bodyLarge
            )

            Box(
                modifier = Modifier
                    .clip(AppCardShape)
                    .background(AppSurfaceRaised)
                    .border(1.dp, AppSurfaceStroke, AppCardShape)
                    .padding(horizontal = 14.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "$activeCount active",
                    color = AppTextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun EmptyStateCard() {
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
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(AppPillShape)
                    .background(AppAccentMuted),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "0",
                    color = AppAccent,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "No active programs yet",
                color = AppTextPrimary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Open any program, generate a plan, and start it to see it here.",
                color = AppTextSecondary,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun ActiveProgramCard(
    program: ActiveProgram,
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
                .background(Brush.linearGradient(listOf(AppSurfaceRaised, AppSurface)))
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
                        text = program.programName,
                        color = AppTextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = program.selectedLift,
                        color = AppAccent,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Started ${formatProgramDate(program.startDate)}",
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
                        text = "${program.generatedPlan.size} blocks",
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
                DetailMetric(modifier = Modifier.weight(1f), label = "1RM", value = "${program.oneRepMax.toInt()} kg")
                DetailMetric(modifier = Modifier.weight(1f), label = "Status", value = "Active")
                DetailMetric(modifier = Modifier.weight(1f), label = "Open", value = "Details")
            }
        }
    }
}

@Composable
private fun DetailMetric(
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

private fun formatProgramDate(date: Long): String {
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(date))
}


