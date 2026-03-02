package com.example.gymapp.screens.programs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymapp.data.Program
import com.example.gymapp.data.WeekPlan
import com.example.gymapp.functions.LiftType
import com.example.gymapp.functions.calculate531
import com.example.gymapp.functions.calculateFiveByFive
import com.example.gymapp.functions.calculateHybrid
import com.example.gymapp.screens.MainScaffold

private val Background = Color(0xFF0A0F1C)
private val Surface = Color(0xFF141B2D)
private val CardColor = Color(0xFF151E2E)
private val Accent = Color(0xFFE53935)
private val TextPrimary = Color.White
private val TextSecondary = Color(0xFF9FA8B3)

private val CardShape = RoundedCornerShape(18.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramDetailScreen(program: Program, navController: NavController) {

    var expandedDescription by remember { mutableStateOf(false) }

    var selectedLift by remember { mutableStateOf("Squat") }
    var expandedDropdown by remember { mutableStateOf(false) }
    var oneRepMaxInput by remember { mutableStateOf("") }
    var calculatedPlan by remember { mutableStateOf<List<WeekPlan>?>(null) }

    val lifts = listOf("Squat", "Bench", "Deadlift")

    MainScaffold(
        navController = navController,
        title = program.name
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp) // ✅ manji razmak
        ) {

            // ✅ 1) Expandable description vraćen
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = CardShape,
                    colors = CardDefaults.cardColors(containerColor = CardColor),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Text(
                            text = "Program Overview",
                            color = TextPrimary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(6.dp))

                        val shortText = if (program.description.length > 170)
                            program.description.take(170) + "…"
                        else program.description

                        Text(
                            text = if (expandedDescription) program.description else shortText,
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(Modifier.height(6.dp))

                        TextButton(
                            onClick = { expandedDescription = !expandedDescription },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = if (expandedDescription) "Show less" else "Read more",
                                color = Accent,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Setup panel (bez blur-a, readable)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = CardShape,
                    colors = CardDefaults.cardColors(containerColor = Surface.copy(alpha = 0.9f)),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp) // ✅ kompaktnije
                    ) {

                        Text(
                            "Setup",
                            color = TextPrimary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        // Lift dropdown
                        ExposedDropdownMenuBox(
                            expanded = expandedDropdown,
                            onExpandedChange = { expandedDropdown = !expandedDropdown }
                        ) {
                            OutlinedTextField(
                                value = selectedLift,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Lift") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expandedDropdown)
                                },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                textStyle = TextStyle(color = TextPrimary),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Accent,
                                    unfocusedBorderColor = Color(0xFF2A3A55),
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = expandedDropdown,
                                onDismissRequest = { expandedDropdown = false }
                            ) {
                                lifts.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it) },
                                        onClick = {
                                            selectedLift = it
                                            expandedDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        // 1RM input
                        OutlinedTextField(
                            value = oneRepMaxInput,
                            onValueChange = { oneRepMaxInput = it.filter { c -> c.isDigit() } },
                            label = { Text("1RM (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            textStyle = TextStyle(color = TextPrimary),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Accent,
                                unfocusedBorderColor = Color(0xFF2A3A55),
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        Button(
                            onClick = {
                                val oneRM = oneRepMaxInput.toDoubleOrNull()
                                if (oneRM != null) {
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
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Accent)
                        ) {
                            Text("Generate", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Result header + compact progress bar
            calculatedPlan?.let { plan ->

                item {
                    Column {
                        Text(
                            text = "Plan",
                            color = TextPrimary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = 1f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp),
                            color = Accent,
                            trackColor = Surface
                        )
                    }
                }

                // ✅ 2) Kompaktne week kartice + ✅ 3) leva linija
                itemsIndexed(plan) { index, week ->
                    CompactWeekCard(
                        title = if (program.id == "five_by_five")
                            "Workout ${week.weekNumber}"
                        else
                            "Week ${week.weekNumber}",
                        week = week,
                        isFiveByFive = (program.id == "five_by_five"),
                        isHybrid = (program.id == "Hybrid_Strength"),
                        is531 = (program.id == "five_three_one"),
                        showTableHeader = (index == 0) // ✅ samo prvi prikazuje header
                    )
                }
            }
        }
    }
}

/**
 * Kompaktna kartica (niža) + leva accent linija.
 * Namerno mala visina da može lakše screenshot celog plana.
 */
@Composable
private fun CompactWeekCard(
    title: String,
    week: WeekPlan,
    isFiveByFive: Boolean,
    isHybrid: Boolean,
    is531: Boolean,              // ✅ NOVO
    showTableHeader: Boolean
) {
    data class RowAgg(val sets: Int, val reps: Int, val weight: Double)

    val aggregated: List<RowAgg> = week.sets
        .groupBy { it.reps to it.weight }
        .map { (key, list) ->
            RowAgg(sets = list.size, reps = key.first, weight = key.second)
        }
        .sortedWith(compareByDescending<RowAgg> { it.weight }.thenByDescending { it.reps })

    // 5/3/1 AMRAP: poslednji (najteži) set u nedeljama 1–3
    val is531AmrapWeek = is531 && (week.weekNumber % 4 != 0) // week 4,8,12,16 = deload (nema +)

    val amrapKey: Pair<Int, Double>? = if (is531AmrapWeek) {
        val heaviest = week.sets.maxByOrNull { it.weight }
        heaviest?.let { it.reps to it.weight }
    } else null

    // "Spojeno" — nema card senke, nema rounded separation
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardColor)
            .padding(horizontal = 12.dp, vertical = 10.dp) // kompaktnije
    ) {

        // Naslov (Workout/Week)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(18.dp)
                    .background(Accent, RoundedCornerShape(10.dp))
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = title,
                color = TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(8.dp))

        // ✅ Header se prikazuje samo jednom (na prvom elementu u listi)
        if (showTableHeader) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "SETS",
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "REPS",
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "WEIGHT",
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(6.dp))
            Divider(color = Color.White.copy(alpha = 0.06f), thickness = 1.dp)
            Spacer(Modifier.height(6.dp))
        } else {
            // mali divider ispod naslova da bude uredno
            Divider(color = Color.White.copy(alpha = 0.05f), thickness = 1.dp)
            Spacer(Modifier.height(6.dp))
        }

        // Redovi (grupisani)
        aggregated.forEachIndexed { idx, row ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = row.sets.toString(),
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // ✅ TOP samo za Hybrid i samo prvi red (najteži)
                    if (isHybrid && idx == 0) {
                        AssistChip(
                            onClick = {},
                            label = { Text("TOP", fontWeight = FontWeight.Bold) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = Accent.copy(alpha = 0.18f),
                                labelColor = TextPrimary
                            )
                        )
                        Spacer(Modifier.width(8.dp))
                    }

                    val isAmrapRow = is531AmrapWeek && amrapKey == (row.reps to row.weight)

                    Text(
                        text = if (isAmrapRow) "${row.reps}+" else row.reps.toString(),
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            "${row.weight} kg",
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Accent.copy(alpha = 0.16f),
                        labelColor = TextPrimary
                    )
                )
            }

            if (idx != aggregated.lastIndex) {
                Divider(
                    color = Color.White.copy(alpha = 0.05f),
                    thickness = 1.dp
                )
            }
        }

        // ✅ Divider na dnu da kartice budu "spojene" (umesto praznog razmaka)
        Spacer(Modifier.height(8.dp))
        Divider(color = Color.White.copy(alpha = 0.06f), thickness = 1.dp)
    }
}