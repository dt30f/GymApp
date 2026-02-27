package com.example.gymapp.screens.programs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymapp.data.Program
import com.example.gymapp.data.WeekPlan
import com.example.gymapp.functions.calculate531
import com.example.gymapp.screens.MainScaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.example.gymapp.functions.calculateFiveByFive
import com.example.gymapp.functions.calculateHybrid


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
        title = program.name,
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {

            // 🔹 Opis programa (expandable)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1c2d4a) // tamnija plava za Card
                    ),
                    border = BorderStroke(2.dp, Color(0xFF491669))

                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = if (expandedDescription)
                                program.description
                            else
                                program.description.take(150) + "...",
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TextButton(
                            onClick = { expandedDescription = !expandedDescription }
                        ) {
                            Text(
                                if (expandedDescription) "Show less"
                                else "Read full description"
                            )
                        }
                    }
                }
            }

            // 🔹 Dropdown za izbor lifta
            item {
                ExposedDropdownMenuBox(
                    expanded = expandedDropdown,
                    onExpandedChange = { expandedDropdown = !expandedDropdown }
                ) {
                    TextField(
                        value = selectedLift,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Lift") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
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
                                }
                            )
                        }
                    }
                }
            }

            // 🔹 Unos 1RM
            item {
                OutlinedTextField(
                    value = oneRepMaxInput,
                    onValueChange = { oneRepMaxInput = it },
                    label = { Text("Enter your 1RM (kg)") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = Color.White),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            // 🔹 Calculate dugme
            item {
                Button(
                    onClick = {
                        val oneRepMax = oneRepMaxInput.toDoubleOrNull()
                        if (oneRepMax != null) {
                            calculatedPlan = when (program.id) {

                                "five_by_five" -> calculateFiveByFive(oneRepMax)

                                "five_three_one" -> calculate531(oneRepMax)

                                "Hybrid_Strength" -> calculateHybrid(oneRepMax)

                                else -> emptyList()
                            }

                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Calculate")
                }
            }

            // 🔥 4 nedeljni pregled
            calculatedPlan?.let { plan ->
                items(plan) { week ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                        shape = RoundedCornerShape(5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1c2d4a)
                        ),
                        border = BorderStroke(2.dp, Color(0xFF491669))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                text = if (program.id == "five_by_five")
                                    "Trening ${week.weekNumber}"
                                else
                                    "Week ${week.weekNumber}"
                                ,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            week.sets.forEach { set ->
                                Text(
                                    text = if(program.id == "five_by_five")
                                            "5 sets x 5 reps @ ${set.weight} kg"
                                        else
                                            "${set.reps} reps @ ${set.weight} kg",
                                    color = Color(0xFFCCCCCC)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}




