package com.example.gymapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

private val Background = Color(0xFF0B1220)
private val TopBarBg = Color(0xFF070E1B)
private val Surface = Color(0xFF111A2E)
private val Surface2 = Color(0xFF141F36)
private val Accent = Color(0xFFE53935)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFAAB3C5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val uiState by userViewModel.uiState.collectAsState()

    var showWeightDialog by remember { mutableStateOf(false) }
    var weightInput by remember { mutableStateOf(TextFieldValue("")) }
    var dialogError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Profile", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Text(
                            "Manage your stats",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = TopBarBg
                )
            )
        }
    ) { padding ->

        when (val state = uiState) {

            is UserUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Accent)
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .background(Background),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    // Main info card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        elevation = CardDefaults.cardElevation(10.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            Text(
                                text = user.name,
                                color = TextPrimary,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Divider(color = TextSecondary.copy(alpha = 0.12f))

                            ProfileRow(label = "Body Weight", value = formatKg(user.bodyWeight))
                            ProfileRow(label = "Squat 1RM", value = "${user.squat1RM} kg")
                            ProfileRow(label = "Bench 1RM", value = "${user.bench1RM} kg")
                            ProfileRow(label = "Deadlift 1RM", value = "${user.deadlift1RM} kg")

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
                                    .height(50.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Accent,
                                    contentColor = TextPrimary
                                )
                            ) {
                                Text("Edit Body Weight", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Actions card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        elevation = CardDefaults.cardElevation(10.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Actions",
                                color = TextPrimary,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Button(
                                onClick = { userViewModel.logout() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = TextPrimary
                                )
                            ) {
                                Text("Logout", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // ✅ Dialog za izmenu telesne težine
                if (showWeightDialog) {
                    AlertDialog(
                        containerColor = Surface2,
                        onDismissRequest = { showWeightDialog = false },
                        title = { Text("Update Body Weight", color = TextPrimary) },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Accent,
                                        unfocusedBorderColor = TextSecondary.copy(alpha = 0.35f),
                                        focusedLabelColor = Accent,
                                        unfocusedLabelColor = TextSecondary,
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary,
                                        cursorColor = Accent
                                    )
                                )

                                if (dialogError != null) {
                                    Text(
                                        text = dialogError!!,
                                        color = Accent,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Text(
                                    text = "Tip: You can enter decimals (e.g. 82.5).",
                                    color = TextSecondary,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val bw = weightInput.text.toFloatOrNull()
                                    if (bw == null || bw <= 0f) {
                                        dialogError = "Enter a valid weight (e.g. 82.5)."
                                        return@TextButton
                                    }

                                    userViewModel.changeBodyWeight(bw)
                                    showWeightDialog = false
                                }
                            ) {
                                Text("Save", color = Accent, fontWeight = FontWeight.Bold)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showWeightDialog = false }) {
                                Text("Cancel", color = TextSecondary)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = TextSecondary,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            color = TextPrimary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun formatKg(value: Float): String {
    if (value <= 0f) return "—"
    val rounded = (value * 10f).roundToInt() / 10f
    return "$rounded kg"
}

private fun filterDecimal(input: String): String {
    val filtered = input.filter { it.isDigit() || it == '.' }
    val dotCount = filtered.count { it == '.' }
    return if (dotCount <= 1) filtered else filtered.replaceFirst(".", "")
}