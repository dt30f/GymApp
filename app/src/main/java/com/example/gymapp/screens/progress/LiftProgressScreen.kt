package com.example.gymapp.progress

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymapp.screens.progress.LiftChart
import com.example.gymapp.viewmodel.LiftViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiftProgressScreen(liftType: String) {

    val context = LocalContext.current
    val viewModel: LiftViewModel = hiltViewModel()
    val lifts by viewModel
        .getLifts(liftType)
        .collectAsState(initial = emptyList())


    val mostRecent by viewModel
        .getMostRecentLift(liftType)
        .collectAsState(initial = 0f)

    val pr by viewModel
        .getPrForLift(liftType)
        .collectAsState(initial = 0)

    val bodyWeight by viewModel
        .getBodyWeight()
        .collectAsState(initial = 0f)

    val ratio = pr/bodyWeight

    var showDialog by remember { mutableStateOf(false) }
    var inputWeight by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        containerColor = Color(0xFF07162e),
        topBar = { TopAppBar(
            title = { Text("$liftType Progress", color = Color.Red) },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color(0xFF070e1b), // isto i za TopAppBar
                titleContentColor = Color.White
            ),
            ) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            if (lifts.isNotEmpty()) {
                LiftChart(lifts)
            } else {
                Text("No data yet. Add your first lift.")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1c2d4a)
                ),
                shape = RoundedCornerShape(6.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                border = BorderStroke(2.dp, Color(0xFF491669)),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem("Body Weight", bodyWeight.toString())
                        StatItem("Most Recent", mostRecent.toString())
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem("PR", pr.toString())
                        StatItem("Ratio", ratio.toString())
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1c2d4a)
                ),
                shape = RoundedCornerShape(6.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                border = BorderStroke(2.dp, Color(0xFF491669))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        "Strength Level",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Red
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    StrengthLevelIndicator(calculateLevel(ratio))
                }
            }


            // Dialog za unos težine
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Add Weight") },
                    text = {
                        TextField(
                            value = inputWeight,
                            onValueChange = { inputWeight = it },
                            label = { Text("Weight (kg)") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            )
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            val weight = inputWeight.text.toFloatOrNull()
                            if (weight != null) {
                                viewModel.addLift(liftType, weight)
                                inputWeight = TextFieldValue("")
                                showDialog = false
                            }
                        }) { Text("Add") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                    }
                )
            }
        }
    }
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
fun StrengthLevelIndicator(level: Int) {

    val colors = listOf(
        Color(0xFF4CAF50), // 1 - Green
        Color(0xFF1B5E20), // 2 - Dark Green
        Color(0xFFFFC107), // 3 - Yellow
        Color(0xFFE53935), // 4 - Red
        Color(0xFF8E24AA)  // 5 - Purple
    )

    Row(
        modifier = Modifier
            .width(140.dp)
            .height(24.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        for (i in 1..5) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        if (i <= level)
                            colors[i - 1]
                        else
                            colors[i - 1].copy(alpha = 0.25f),
                        shape = RoundedCornerShape(6.dp)
                    )
            )
        }
    }
}


@Composable
fun StatItem(label: String, value: String) {

    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Yellow
        )
    }
}

