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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gymapp.viewmodel.UserUiState
import com.example.gymapp.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController
) {
    val userViewModel: UserViewModel = hiltViewModel()

    var name by remember { mutableStateOf("") }
    var bodyWeight by remember { mutableStateOf("") }
    var squat by remember { mutableStateOf("") }
    var bench by remember { mutableStateOf("") }
    var deadlift by remember { mutableStateOf("") }

    var isSubmitting by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }

    val userState by userViewModel.uiState.collectAsState()

    LaunchedEffect(userState) {
        if (userState is UserUiState.HasUser) {
            navController.navigate("home") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    val bwValue = bodyWeight.toFloatOrNull()
    val squatValue = squat.toIntOrNull()
    val benchValue = bench.toIntOrNull()
    val deadliftValue = deadlift.toIntOrNull()

    val isFormValid =
        name.isNotBlank() &&
            bwValue != null && bwValue > 0f &&
            squatValue != null && squatValue > 0 &&
            benchValue != null && benchValue > 0 &&
            deadliftValue != null && deadliftValue > 0

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Create Profile",
                            color = AppTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Set your current numbers to personalize training.",
                            color = AppTextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTopBar,
                    titleContentColor = AppTextPrimary
                )
            )
        }
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
                        Text(
                            text = "Start with a clean baseline.",
                            color = AppTextPrimary,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Your bodyweight and estimated one-rep maxes are used across the app for progress and planning.",
                            color = AppTextSecondary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
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
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Athlete Details",
                            color = AppTextPrimary,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        StyledField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Name",
                            placeholder = "e.g. Djordje",
                            keyboardType = KeyboardType.Text
                        )

                        StyledField(
                            value = bodyWeight,
                            onValueChange = { bodyWeight = filterDecimal(it) },
                            label = "Body Weight (kg)",
                            placeholder = "e.g. 82.5",
                            keyboardType = KeyboardType.Decimal
                        )

                        Divider(color = AppSurfaceStroke, thickness = 1.dp)

                        Text(
                            text = "Strength Numbers",
                            color = AppTextPrimary,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            MetricPreview(modifier = Modifier.weight(1f), label = "Squat", value = squat.ifBlank { "--" })
                            MetricPreview(modifier = Modifier.weight(1f), label = "Bench", value = bench.ifBlank { "--" })
                            MetricPreview(modifier = Modifier.weight(1f), label = "Deadlift", value = deadlift.ifBlank { "--" })
                        }

                        StyledField(
                            value = squat,
                            onValueChange = { squat = it.filter(Char::isDigit) },
                            label = "Squat 1RM (kg)",
                            placeholder = "e.g. 180",
                            keyboardType = KeyboardType.Number
                        )

                        StyledField(
                            value = bench,
                            onValueChange = { bench = it.filter(Char::isDigit) },
                            label = "Bench 1RM (kg)",
                            placeholder = "e.g. 130",
                            keyboardType = KeyboardType.Number
                        )

                        StyledField(
                            value = deadlift,
                            onValueChange = { deadlift = it.filter(Char::isDigit) },
                            label = "Deadlift 1RM (kg)",
                            placeholder = "e.g. 220",
                            keyboardType = KeyboardType.Number
                        )

                        if (errorText != null) {
                            Text(
                                text = errorText.orEmpty(),
                                color = AppAccent,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Button(
                            onClick = {
                                errorText = null
                                if (!isFormValid) {
                                    errorText = "Please fill all fields with valid values."
                                    return@Button
                                }

                                isSubmitting = true
                                userViewModel.registerUser(
                                    name = name.trim(),
                                    squat1RM = squatValue!!,
                                    bench1RM = benchValue!!,
                                    deadlift1RM = deadliftValue!!,
                                    bodyWeight = bwValue!!
                                )
                                isSubmitting = false
                            },
                            enabled = isFormValid && !isSubmitting,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppAccent,
                                contentColor = AppTextPrimary,
                                disabledContainerColor = AppAccent.copy(alpha = 0.35f),
                                disabledContentColor = AppTextPrimary.copy(alpha = 0.7f)
                            ),
                            shape = AppCardShape
                        ) {
                            if (isSubmitting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.height(18.dp),
                                    strokeWidth = 2.dp,
                                    color = AppTextPrimary
                                )
                                Text(" Saving...", fontWeight = FontWeight.Bold)
                            } else {
                                Text("Start Training", fontWeight = FontWeight.Bold)
                            }
                        }

                        Text(
                            text = "You can update bodyweight later from your profile screen.",
                            color = AppTextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricPreview(
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

@Composable
private fun StyledField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        shape = AppCardShape,
        textStyle = TextStyle(color = AppTextPrimary, fontWeight = FontWeight.SemiBold),
        colors = inputColors()
    )
}

@Composable
private fun inputColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedContainerColor = AppSurfaceRaised,
        unfocusedContainerColor = AppSurfaceRaised,
        focusedBorderColor = AppAccent,
        unfocusedBorderColor = AppSurfaceStroke,
        focusedLabelColor = AppAccent,
        unfocusedLabelColor = AppTextMuted,
        focusedTextColor = AppTextPrimary,
        unfocusedTextColor = AppTextPrimary,
        cursorColor = AppAccent
    )
}

private fun filterDecimal(input: String): String {
    val filtered = input.filter { it.isDigit() || it == '.' }
    val dotCount = filtered.count { it == '.' }
    return if (dotCount <= 1) filtered else filtered.replaceFirst(".", "")
}

