package com.example.gymapp.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymapp.screens.progress.LiftChart
import com.example.gymapp.viewmodel.LiftViewModel
import kotlin.math.roundToInt

private val Background = Color(0xFF0B1220)
private val TopBarBg = Color(0xFF070E1B)
private val Surface = Color(0xFF111A2E)
private val Surface2 = Color(0xFF141F36)
private val Accent = Color(0xFFE53935)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFAAB3C5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiftProgressScreen(liftType: String) {

    val context = LocalContext.current
    val viewModel: LiftViewModel = hiltViewModel()

    val lifts by viewModel.getLifts(liftType).collectAsState(initial = emptyList())
    val mostRecent by viewModel.getMostRecentLift(liftType).collectAsState(initial = 0f)
    val pr by viewModel.getPrForLift(liftType).collectAsState(initial = 0)
    val bodyWeight by viewModel.getBodyWeight().collectAsState(initial = 0f)

    val ratio = if (bodyWeight > 0f) pr / bodyWeight else 0f

    var showDialog by remember { mutableStateOf(false) }
    var inputWeight by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = liftType,
                            color = TextPrimary,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Progress",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = TopBarBg
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Accent,
                contentColor = TextPrimary
            ) {
                Text("+", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            // Chart container (ne diramo chart logiku, samo okvir)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Surface),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    if (lifts.isNotEmpty()) {
                        LiftChart(lifts)
                    } else {
                        Text(
                            "No data yet. Add your first lift.",
                            color = TextSecondary,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Stats (2x2 grid)
            StatsCard(
                bodyWeight = bodyWeight,
                mostRecent = mostRecent,
                pr = pr,
                ratio = ratio
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Strength Level (serious)
            StrengthCard(ratio = ratio)

            // Dialog
            if (showDialog) {
                AlertDialog(
                    containerColor = Surface2,
                    onDismissRequest = { showDialog = false },
                    title = { Text("Add Lift", color = TextPrimary) },
                    text = {
                        OutlinedTextField(
                            value = inputWeight,
                            onValueChange = { inputWeight = it },
                            label = { Text("Weight (kg)") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Accent,
                                unfocusedBorderColor = TextSecondary.copy(alpha = 0.35f),
                                focusedLabelColor = Accent,
                                unfocusedLabelColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val weight = inputWeight.text.toFloatOrNull()
                                if (weight != null) {
                                    viewModel.addLift(liftType, weight)
                                    inputWeight = TextFieldValue("")
                                    showDialog = false
                                }
                            }
                        ) {
                            Text("Add", color = Accent, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancel", color = TextSecondary)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun StatsCard(
    bodyWeight: Float,
    mostRecent: Float,
    pr: Int,
    ratio: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                text = "Summary",
                color = TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(10.dp))

            // 2x2 grid
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatBlock("Body Weight", formatKg(bodyWeight))
                StatBlock("Most Recent", formatKg(mostRecent))
            }

            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatBlock("PR", "${pr} kg")
                StatBlock("Ratio", String.format("%.2f", ratio))
            }
        }
    }
}

@Composable
private fun StatBlock(label: String, value: String) {
    Column(modifier = Modifier.widthIn(min = 140.dp)) {
        Text(
            text = label.uppercase(),
            color = TextSecondary,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            color = TextPrimary,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun StrengthCard(ratio: Float) {
    val level = calculateLevel(ratio)
    val label = when (level) {
        1 -> "Beginner"
        2 -> "Novice"
        3 -> "Intermediate"
        4 -> "Advanced"
        else -> "Elite"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Strength Level",
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                AssistChip(
                    onClick = {},
                    label = {
                        Text(label, fontWeight = FontWeight.Bold)
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Accent.copy(alpha = 0.18f),
                        labelColor = TextPrimary
                    )
                )
            }

            Spacer(Modifier.height(12.dp))

            // Pro bar (clean)
            LinearProgressIndicator(
                progress = (level / 5f).coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Accent,
                trackColor = TextSecondary.copy(alpha = 0.15f)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Based on PR / body weight",
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun formatKg(value: Float): String {
    if (value <= 0f) return "—"
    val rounded = (value * 10f).roundToInt() / 10f
    return "$rounded kg"
}

fun calculateLevel(ratio: Float): Int {
    return when {
        ratio < 0.75f -> 1
        ratio < 1.0f -> 2
        ratio < 1.5f -> 3
        ratio < 2.0f -> 4
        else -> 5
    }
}