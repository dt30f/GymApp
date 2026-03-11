package com.example.gymapp.screens.programs

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gymapp.data.Program
import com.example.gymapp.data.WeekPlan
import com.example.gymapp.functions.LiftType
import com.example.gymapp.functions.calculate531
import com.example.gymapp.functions.calculateFiveByFive
import com.example.gymapp.functions.calculateHybrid
import com.example.gymapp.screens.AppAccent
import com.example.gymapp.screens.AppAccentMuted
import com.example.gymapp.screens.AppBackground
import com.example.gymapp.screens.AppCardShape
import com.example.gymapp.screens.AppLargeCardShape
import com.example.gymapp.screens.AppPillShape
import com.example.gymapp.screens.AppSuccess
import com.example.gymapp.screens.AppSurface
import com.example.gymapp.screens.AppSurfaceRaised
import com.example.gymapp.screens.AppSurfaceStroke
import com.example.gymapp.screens.AppTextMuted
import com.example.gymapp.screens.AppTextPrimary
import com.example.gymapp.screens.AppTextSecondary
import com.example.gymapp.screens.MainScaffold
import com.example.gymapp.viewmodel.ActiveProgramsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramDetailScreen(
    program: Program,
    navController: NavController,
    viewModel: ActiveProgramsViewModel = hiltViewModel()
) {
    var expandedDescription by remember { mutableStateOf(false) }
    var selectedLift by remember { mutableStateOf("Squat") }
    var expandedDropdown by remember { mutableStateOf(false) }
    var oneRepMaxInput by remember { mutableStateOf("") }
    var calculatedPlan by remember { mutableStateOf<List<WeekPlan>?>(null) }

    val startState by viewModel.startProgramState.collectAsState()

    val lifts = listOf("Squat", "Bench", "Deadlift")
    val programType = when (program.id) {
        "five_by_five" -> "Strength Base"
        "five_three_one" -> "Power Cycle"
        "Hybrid_Strength" -> "Hybrid Peak"
        else -> "Performance"
    }

    val oneRepMaxValue = oneRepMaxInput.toDoubleOrNull()

    LaunchedEffect(program.id) {
        viewModel.clearStartProgramFeedback()
    }

    MainScaffold(
        navController = navController,
        title = program.name
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
                HeroSection(
                    program = program,
                    programType = programType,
                    calculatedPlan = calculatedPlan
                )
            }

            item {
                OverviewCard(
                    description = program.description,
                    expandedDescription = expandedDescription,
                    onToggleDescription = { expandedDescription = !expandedDescription }
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = AppLargeCardShape,
                    colors = CardDefaults.cardColors(containerColor = AppSurface),
                    border = BorderStroke(1.dp, AppSurfaceStroke),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Build Training Plan",
                                color = AppTextPrimary,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Choose a lift, enter a one-rep max, generate the plan, then start the cycle.",
                                color = AppTextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        InfoPillRow(
                            leftLabel = "Program",
                            leftValue = programType,
                            rightLabel = "Lift",
                            rightValue = selectedLift
                        )

                        ExposedDropdownMenuBox(
                            expanded = expandedDropdown,
                            onExpandedChange = { expandedDropdown = !expandedDropdown }
                        ) {
                            OutlinedTextField(
                                value = selectedLift,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Primary lift") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expandedDropdown)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = AppCardShape,
                                textStyle = TextStyle(color = AppTextPrimary),
                                colors = inputColors()
                            )

                            ExposedDropdownMenu(
                                expanded = expandedDropdown,
                                onDismissRequest = { expandedDropdown = false }
                            ) {
                                lifts.forEach { lift ->
                                    DropdownMenuItem(
                                        text = { Text(lift) },
                                        onClick = {
                                            selectedLift = lift
                                            expandedDropdown = false
                                            calculatedPlan = null
                                            viewModel.clearStartProgramFeedback()
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = oneRepMaxInput,
                            onValueChange = {
                                oneRepMaxInput = it.filter(Char::isDigit)
                                calculatedPlan = null
                                viewModel.clearStartProgramFeedback()
                            },
                            label = { Text("One-rep max") },
                            suffix = {
                                Text(
                                    text = "kg",
                                    color = AppTextMuted,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = AppCardShape,
                            textStyle = TextStyle(
                                color = AppTextPrimary,
                                fontWeight = FontWeight.SemiBold
                            ),
                            colors = inputColors()
                        )

                        if (startState.message != null) {
                            FeedbackCard(
                                message = startState.message!!,
                                isSuccess = startState.isSuccess
                            )
                        }

                        Button(
                            onClick = {
                                val oneRM = oneRepMaxValue
                                if (oneRM != null) {
                                    viewModel.clearStartProgramFeedback()
                                    calculatedPlan = when (program.id) {
                                        "five_by_five" -> calculateFiveByFive(oneRM)
                                        "five_three_one" -> calculate531(
                                            oneRM,
                                            when (selectedLift) {
                                                "Bench" -> LiftType.BENCH
                                                "Deadlift" -> LiftType.DEADLIFT
                                                else -> LiftType.SQUAT
                                            }
                                        )
                                        "Hybrid_Strength" -> calculateHybrid(oneRM)
                                        else -> emptyList()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = AppCardShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppAccent,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Generate Plan",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Button(
                            onClick = {
                                val plan = calculatedPlan
                                val oneRM = oneRepMaxValue
                                if (plan != null && oneRM != null) {
                                    viewModel.startProgram(
                                        programId = program.id,
                                        programName = program.name,
                                        selectedLift = selectedLift,
                                        oneRepMax = oneRM,
                                        generatedPlan = plan
                                    )
                                }
                            },
                            enabled = calculatedPlan != null && oneRepMaxValue != null && !startState.isSaving,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = AppCardShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppSuccess,
                                contentColor = Color.Black,
                                disabledContainerColor = AppSuccess.copy(alpha = 0.35f),
                                disabledContentColor = Color.Black.copy(alpha = 0.7f)
                            )
                        ) {
                            Text(
                                text = if (startState.isSaving) "Starting..." else "Start Program",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            calculatedPlan?.let { plan ->
                item {
                    PlanHeader(
                        plan = plan,
                        isFiveByFive = program.id == "five_by_five"
                    )
                }

                itemsIndexed(plan) { index, week ->
                    ProgramWeekCard(
                        title = if (program.id == "five_by_five") "Workout ${week.weekNumber}" else "Week ${week.weekNumber}",
                        subtitle = when {
                            program.id == "five_by_five" -> "Volume-driven 5x5 progression"
                            program.id == "Hybrid_Strength" -> "Top set plus volume work"
                            program.id == "five_three_one" && week.weekNumber % 4 == 0 -> "Deload and reset"
                            else -> "Intensity-focused strength work"
                        },
                        week = week,
                        isHybrid = program.id == "Hybrid_Strength",
                        is531 = program.id == "five_three_one",
                        showHeader = index == 0
                    )
                }
            }
        }
    }
}

@Composable
private fun FeedbackCard(
    message: String,
    isSuccess: Boolean
) {
    val accent = if (isSuccess) AppSuccess else AppAccent
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = AppCardShape,
        colors = CardDefaults.cardColors(containerColor = accent.copy(alpha = 0.14f)),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = message,
            color = if (isSuccess) AppSuccess else AppAccent,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun HeroSection(
    program: Program,
    programType: String,
    calculatedPlan: List<WeekPlan>?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = AppLargeCardShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = Brush.verticalGradient(colors = listOf(AppSurfaceRaised, AppSurface)))
                .padding(22.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(AppPillShape)
                    .background(AppAccentMuted)
                    .border(1.dp, Color.White.copy(alpha = 0.08f), AppPillShape)
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(
                    text = programType.uppercase(),
                    color = AppAccent,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = program.name,
                color = AppTextPrimary,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Structured strength progression built for disciplined training and clean execution.",
                color = AppTextSecondary,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HeroStatCard(modifier = Modifier.weight(1f), label = "Exercises", value = program.exercises.size.toString())
                HeroStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Duration",
                    value = calculatedPlan?.size?.toString() ?: when (program.id) {
                        "five_by_five" -> "16"
                        "five_three_one" -> "16"
                        else -> "4"
                    }
                )
                HeroStatCard(
                    modifier = Modifier.weight(1f),
                    label = if (program.id == "five_by_five") "Format" else "Focus",
                    value = if (program.id == "five_by_five") "5x5" else "Strength"
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
            .clip(AppCardShape)
            .background(Color.White.copy(alpha = 0.04f))
            .border(1.dp, Color.White.copy(alpha = 0.06f), AppCardShape)
            .padding(horizontal = 14.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = label.uppercase(), color = AppTextMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
        Text(text = value, color = AppTextPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun OverviewCard(
    description: String,
    expandedDescription: Boolean,
    onToggleDescription: () -> Unit
) {
    val shortText = if (description.length > 200) description.take(200) + "..." else description

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = AppLargeCardShape,
        colors = CardDefaults.cardColors(containerColor = AppSurfaceRaised),
        border = BorderStroke(1.dp, AppSurfaceStroke),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = "Program Overview", color = AppTextPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = if (expandedDescription) description else shortText, color = AppTextSecondary, style = MaterialTheme.typography.bodyLarge)

            if (description.length > 200) {
                TextButton(onClick = onToggleDescription, contentPadding = PaddingValues(0.dp)) {
                    Text(
                        text = if (expandedDescription) "Show less" else "Read full overview",
                        color = AppAccent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoPillRow(
    leftLabel: String,
    leftValue: String,
    rightLabel: String,
    rightValue: String
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        MetricPill(modifier = Modifier.weight(1f), label = leftLabel, value = leftValue)
        MetricPill(modifier = Modifier.weight(1f), label = rightLabel, value = rightValue)
    }
}

@Composable
private fun MetricPill(
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
        Text(text = label.uppercase(), color = AppTextMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
        Text(text = value, color = AppTextPrimary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun PlanHeader(
    plan: List<WeekPlan>,
    isFiveByFive: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "Training Plan", color = AppTextPrimary, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(
                    text = if (isFiveByFive) "${plan.size} sessions of progressive volume" else "${plan.size} weeks of structured loading",
                    color = AppTextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Box(
                modifier = Modifier.size(38.dp).clip(CircleShape).background(AppAccentMuted),
                contentAlignment = Alignment.Center
            ) {
                Text(text = plan.size.toString(), color = AppAccent, fontWeight = FontWeight.Bold)
            }
        }

        LinearProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(AppPillShape),
            color = AppAccent,
            trackColor = AppSurfaceStroke
        )
    }
}

@Composable
private fun ProgramWeekCard(
    title: String,
    subtitle: String,
    week: WeekPlan,
    isHybrid: Boolean,
    is531: Boolean,
    showHeader: Boolean
) {
    data class RowAgg(val sets: Int, val reps: Int, val weight: Double)

    val aggregated = week.sets
        .groupBy { it.reps to it.weight }
        .map { (key, list) -> RowAgg(sets = list.size, reps = key.first, weight = key.second) }
        .sortedWith(compareByDescending<RowAgg> { it.weight }.thenByDescending { it.reps })

    val is531AmrapWeek = is531 && (week.weekNumber % 4 != 0)
    val amrapKey = if (is531AmrapWeek) {
        week.sets.maxByOrNull { it.weight }?.let { it.reps to it.weight }
    } else {
        null
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = AppLargeCardShape,
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        border = BorderStroke(1.dp, AppSurfaceStroke),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = title, color = AppTextPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(text = subtitle, color = AppTextSecondary, style = MaterialTheme.typography.bodyMedium)
                }

                Box(
                    modifier = Modifier.clip(AppPillShape).background(AppAccentMuted).padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "${aggregated.sumOf { it.sets }} sets",
                        color = AppAccent,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier
                    .clip(AppCardShape)
                    .background(AppSurfaceRaised)
                    .border(1.dp, AppSurfaceStroke, AppCardShape)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (showHeader) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        HeaderLabel("SETS")
                        HeaderLabel("REPS")
                        HeaderLabel("LOAD")
                    }
                    Divider(color = AppSurfaceStroke, thickness = 1.dp)
                }

                aggregated.forEachIndexed { index, row ->
                    val isAmrapRow = is531AmrapWeek && amrapKey == (row.reps to row.weight)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DataCell(modifier = Modifier.width(58.dp), text = row.sets.toString(), emphasized = true)

                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (isHybrid && index == 0) {
                                AssistChip(
                                    onClick = {},
                                    label = { Text(text = "TOP", fontWeight = FontWeight.Bold) },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = AppSuccess.copy(alpha = 0.16f),
                                        labelColor = AppSuccess
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }

                            DataCell(text = if (isAmrapRow) "${row.reps}+" else row.reps.toString(), accent = isAmrapRow)
                        }

                        Box(
                            modifier = Modifier.clip(AppPillShape).background(AppAccentMuted).padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "${row.weight} kg",
                                color = AppTextPrimary,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (index != aggregated.lastIndex) {
                        Divider(color = AppSurfaceStroke, thickness = 1.dp)
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderLabel(text: String) {
    Text(text = text, color = AppTextMuted, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
}

@Composable
private fun DataCell(
    modifier: Modifier = Modifier,
    text: String,
    emphasized: Boolean = false,
    accent: Boolean = false
) {
    Text(
        text = text,
        modifier = modifier,
        color = when {
            accent -> AppAccent
            emphasized -> AppTextPrimary
            else -> AppTextSecondary
        },
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = if (emphasized || accent) FontWeight.Bold else FontWeight.Medium
    )
}

@Composable
private fun inputColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedContainerColor = AppSurfaceRaised,
        unfocusedContainerColor = AppSurfaceRaised,
        disabledContainerColor = AppSurfaceRaised.copy(alpha = 0.7f),
        focusedBorderColor = AppAccent,
        unfocusedBorderColor = AppSurfaceStroke,
        disabledBorderColor = AppSurfaceStroke,
        focusedTextColor = AppTextPrimary,
        unfocusedTextColor = AppTextPrimary,
        disabledTextColor = AppTextMuted,
        focusedLabelColor = AppAccent,
        unfocusedLabelColor = AppTextMuted,
        disabledLabelColor = AppTextMuted,
        cursorColor = AppAccent
    )
}

