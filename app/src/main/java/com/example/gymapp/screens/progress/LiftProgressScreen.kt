package com.example.gymapp.progress

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymapp.screens.progress.LiftChart
import com.example.gymapp.viewmodel.LiftViewModel
import kotlin.math.roundToInt

private val Background = Color(0xFF070B14)
private val HeroTop = Color(0xFF181F33)
private val HeroBottom = Color(0xFF0C111D)
private val Surface = Color(0xFF101725)
private val SurfaceRaised = Color(0xFF141D2E)
private val SurfaceStroke = Color(0xFF1E2940)
private val Accent = Color(0xFFFF5A36)
private val AccentMuted = Color(0x26FF5A36)
private val Success = Color(0xFF3DDC97)
private val TextPrimary = Color(0xFFF4F7FB)
private val TextSecondary = Color(0xFF98A4B8)
private val TextMuted = Color(0xFF657188)

private val LargeCardShape = RoundedCornerShape(28.dp)
private val CardShape = RoundedCornerShape(22.dp)
private val PillShape = RoundedCornerShape(999.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiftProgressScreen(liftType: String) {
    val viewModel: LiftViewModel = hiltViewModel()

    val lifts by viewModel.getLifts(liftType).collectAsState(initial = emptyList())
    val mostRecent by viewModel.getMostRecentLift(liftType).collectAsState(initial = 0f)
    val pr by viewModel.getPrForLift(liftType).collectAsState(initial = 0)
    val bodyWeight by viewModel.getBodyWeight().collectAsState(initial = 0f)

    val ratio = if (bodyWeight > 0f) pr / bodyWeight else 0f
    val level = calculateLevel(ratio)
    val strengthLabel = levelLabel(level)

    var showDialog by remember { mutableStateOf(false) }
    var inputWeight by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = liftType,
                            color = TextPrimary,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Strength Progress",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                    titleContentColor = TextPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                LiftHeroCard(
                    liftType = liftType,
                    pr = pr,
                    mostRecent = mostRecent,
                    strengthLabel = strengthLabel
                )
            }

            item {
                ChartCard(
                    liftsCount = lifts.size,
                    content = {
                        if (lifts.isNotEmpty()) {
                            LiftChart(lifts)
                        } else {
                            EmptyChartState()
                        }
                    }
                )
            }

            item {
                SummaryCard(
                    bodyWeight = bodyWeight,
                    mostRecent = mostRecent,
                    pr = pr,
                    ratio = ratio
                )
            }

            item {
                StrengthLevelCard(
                    ratio = ratio,
                    strengthLabel = strengthLabel,
                    level = level
                )
            }

            item {
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = CardShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Accent,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Add New Lift",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                containerColor = Surface,
                shape = LargeCardShape,
                title = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Log Lift Entry",
                            color = TextPrimary,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Add a new result for $liftType and keep your progression up to date.",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                text = {
                    OutlinedTextField(
                        value = inputWeight,
                        onValueChange = { inputWeight = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Weight") },
                        suffix = {
                            Text(
                                text = "kg",
                                color = TextMuted,
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        shape = CardShape,
                        textStyle = TextStyle(
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        colors = inputColors()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val weight = inputWeight.text.toFloatOrNull()
                            if (weight != null) {
                                viewModel.addLift(liftType, weight)
                                inputWeight = TextFieldValue("")
                                showDialog = false
                            }
                        },
                        shape = PillShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Accent,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(
                            text = "Cancel",
                            color = TextSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun LiftHeroCard(
    liftType: String,
    pr: Int,
    mostRecent: Float,
    strengthLabel: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = LargeCardShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(HeroTop, HeroBottom)
                    )
                )
                .padding(22.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(PillShape)
                    .background(AccentMuted)
                    .border(1.dp, Color.White.copy(alpha = 0.08f), PillShape)
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(
                    text = "PROGRESS TRACKING",
                    color = Accent,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = liftType,
                color = TextPrimary,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Monitor your latest performance, personal records, and relative strength in one place.",
                color = TextSecondary,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HeroStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Current PR",
                    value = if (pr > 0) "$pr kg" else "—"
                )
                HeroStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Latest",
                    value = formatKg(mostRecent)
                )
                HeroStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Level",
                    value = strengthLabel
                )
            }
        }
    }
}

@Composable
private fun HeroStatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Column(
        modifier = modifier
            .clip(CardShape)
            .background(Color.White.copy(alpha = 0.04f))
            .border(1.dp, Color.White.copy(alpha = 0.06f), CardShape)
            .padding(horizontal = 14.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label.uppercase(),
            color = TextMuted,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ChartCard(
    liftsCount: Int,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = LargeCardShape,
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = BorderStroke(1.dp, SurfaceStroke),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Performance Curve",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (liftsCount > 0) {
                            "$liftsCount logged entries"
                        } else {
                            "No entries logged yet"
                        },
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(PillShape)
                        .background(AccentMuted)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = liftsCount.toString(),
                        color = Accent,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier
                    .clip(CardShape)
                    .background(SurfaceRaised)
                    .border(1.dp, SurfaceStroke, CardShape)
                    .padding(12.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun EmptyChartState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(AccentMuted),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "0",
                color = Accent,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No progress data yet",
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Log your first set result to unlock the chart and start tracking your trend.",
            color = TextSecondary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun SummaryCard(
    bodyWeight: Float,
    mostRecent: Float,
    pr: Int,
    ratio: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = LargeCardShape,
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = BorderStroke(1.dp, SurfaceStroke),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Summary",
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Key numbers for this lift and your current bodyweight context.",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Body Weight",
                    value = formatKg(bodyWeight)
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Most Recent",
                    value = formatKg(mostRecent)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    label = "Personal Record",
                    value = if (pr > 0) "$pr kg" else "—"
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    label = "PR / BW",
                    value = if (ratio > 0f) String.format("%.2f", ratio) else "—"
                )
            }
        }
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Column(
        modifier = modifier
            .clip(CardShape)
            .background(SurfaceRaised)
            .border(1.dp, SurfaceStroke, CardShape)
            .padding(horizontal = 14.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label.uppercase(),
            color = TextMuted,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            color = TextPrimary,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun StrengthLevelCard(
    ratio: Float,
    strengthLabel: String,
    level: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = LargeCardShape,
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = BorderStroke(1.dp, SurfaceStroke),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Strength Level",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Estimated from your PR relative to bodyweight.",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = strengthLabel,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (level >= 4) Success.copy(alpha = 0.16f) else AccentMuted,
                        labelColor = if (level >= 4) Success else Accent
                    )
                )
            }

            LinearProgressIndicator(
                progress = strengthProgress(ratio),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(PillShape),
                color = if (level >= 4) Success else Accent,
                trackColor = SurfaceStroke
            )

            Divider(color = SurfaceStroke, thickness = 1.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StagePill(
                    modifier = Modifier.weight(1f),
                    title = "Current Ratio",
                    value = if (ratio > 0f) String.format("%.2f", ratio) else "—"
                )
                StagePill(
                    modifier = Modifier.weight(1f),
                    title = "Tier",
                    value = "$level / 5"
                )
            }
        }
    }
}

@Composable
private fun StagePill(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Column(
        modifier = modifier
            .clip(CardShape)
            .background(SurfaceRaised)
            .border(1.dp, SurfaceStroke, CardShape)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title.uppercase(),
            color = TextMuted,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun levelLabel(level: Int): String {
    return when (level) {
        1 -> "Beginner"
        2 -> "Novice"
        3 -> "Intermediate"
        4 -> "Advanced"
        else -> "Elite"
    }
}

private fun strengthProgress(ratio: Float): Float {
    return (ratio / 2.5f).coerceIn(0f, 1f)
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

@Composable
private fun inputColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedContainerColor = SurfaceRaised,
        unfocusedContainerColor = SurfaceRaised,
        disabledContainerColor = SurfaceRaised.copy(alpha = 0.7f),
        focusedBorderColor = Accent,
        unfocusedBorderColor = SurfaceStroke,
        disabledBorderColor = SurfaceStroke,
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary,
        disabledTextColor = TextMuted,
        focusedLabelColor = Accent,
        unfocusedLabelColor = TextMuted,
        disabledLabelColor = TextMuted,
        cursorColor = Accent
    )
}

