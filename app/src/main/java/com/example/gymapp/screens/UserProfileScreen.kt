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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gymapp.viewmodel.UserUiState
import com.example.gymapp.viewmodel.UserViewModel
import kotlin.math.roundToInt

@Composable
fun UserProfileScreen(
    navController: NavController
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val uiState by userViewModel.uiState.collectAsState()

    var showWeightDialog by remember { mutableStateOf(false) }
    var weightInput by remember { mutableStateOf(TextFieldValue("")) }
    var dialogError by remember { mutableStateOf<String?>(null) }

    MainScaffold(
        navController = navController,
        title = "Profile"
    ) { padding ->
        when (val state = uiState) {
            is UserUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppBackground)
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppAccent)
                }
            }

            is UserUiState.NoUser -> {
                LaunchedEffect(Unit) {
                    navController.navigate("register") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }

            is UserUiState.HasUser -> {
                val user = state.user
                val total = user.squat1RM + user.bench1RM + user.deadlift1RM

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
                                modifier = Modifier.padding(22.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = user.name,
                                    color = AppTextPrimary,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "Your strength profile and baseline numbers in one place.",
                                    color = AppTextSecondary,
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    ProfileMetric(modifier = Modifier.weight(1f), label = "Bodyweight", value = formatKg(user.bodyWeight))
                                    ProfileMetric(modifier = Modifier.weight(1f), label = "Total", value = "$total kg")
                                    ProfileMetric(modifier = Modifier.weight(1f), label = "Status", value = "Active")
                                }
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
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Text(
                                    text = "Strength Overview",
                                    color = AppTextPrimary,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                LiftProfileRow("Squat 1RM", "${user.squat1RM} kg")
                                LiftProfileRow("Bench 1RM", "${user.bench1RM} kg")
                                LiftProfileRow("Deadlift 1RM", "${user.deadlift1RM} kg")
                                LiftProfileRow("Body Weight", formatKg(user.bodyWeight))

                                Button(
                                    onClick = {
                                        dialogError = null
                                        weightInput = TextFieldValue(
                                            if (user.bodyWeight > 0f) user.bodyWeight.toString() else ""
                                        )
                                        showWeightDialog = true
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = AppCardShape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AppAccent,
                                        contentColor = AppTextPrimary
                                    )
                                ) {
                                    Text("Edit Body Weight", fontWeight = FontWeight.Bold)
                                }
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
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Text(
                                    text = "Account",
                                    color = AppTextPrimary,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                AssistChip(
                                    onClick = {},
                                    label = { Text("Profile Synced", fontWeight = FontWeight.Bold) },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = AppSuccess.copy(alpha = 0.16f),
                                        labelColor = AppSuccess
                                    )
                                )

                                Button(
                                    onClick = { userViewModel.logout() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = AppCardShape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error,
                                        contentColor = AppTextPrimary
                                    )
                                ) {
                                    Text("Logout", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                if (showWeightDialog) {
                    AlertDialog(
                        containerColor = AppSurface,
                        shape = AppLargeCardShape,
                        onDismissRequest = { showWeightDialog = false },
                        title = {
                            Text(
                                text = "Update Body Weight",
                                color = AppTextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                OutlinedTextField(
                                    value = weightInput,
                                    onValueChange = {
                                        weightInput = TextFieldValue(filterDecimal(it.text))
                                    },
                                    label = { Text("Body Weight (kg)") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal,
                                        imeAction = ImeAction.Done
                                    ),
                                    shape = AppCardShape,
                                    textStyle = TextStyle(
                                        color = AppTextPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    colors = inputColors()
                                )

                                if (dialogError != null) {
                                    Text(
                                        text = dialogError.orEmpty(),
                                        color = AppAccent,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Text(
                                    text = "Decimals are allowed, for example 82.5.",
                                    color = AppTextSecondary,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    val bw = weightInput.text.toFloatOrNull()
                                    if (bw == null || bw <= 0f) {
                                        dialogError = "Enter a valid weight."
                                        return@Button
                                    }

                                    userViewModel.changeBodyWeight(bw)
                                    showWeightDialog = false
                                },
                                shape = AppPillShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppAccent,
                                    contentColor = AppTextPrimary
                                )
                            ) {
                                Text("Save", fontWeight = FontWeight.Bold)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showWeightDialog = false }) {
                                Text("Cancel", color = AppTextSecondary)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileMetric(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Column(
        modifier = modifier
            .background(AppSurfaceRaised, AppCardShape)
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
private fun LiftProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppSurfaceRaised, AppCardShape)
            .border(1.dp, AppSurfaceStroke, AppCardShape)
            .padding(horizontal = 14.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = AppTextSecondary,
            style = MaterialTheme.typography.bodyLarge
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

private fun formatKg(value: Float): String {
    if (value <= 0f) return "�"
    val rounded = (value * 10f).roundToInt() / 10f
    return "$rounded kg"
}

private fun filterDecimal(input: String): String {
    val filtered = input.filter { it.isDigit() || it == '.' }
    val dotCount = filtered.count { it == '.' }
    return if (dotCount <= 1) filtered else filtered.replaceFirst(".", "")
}

