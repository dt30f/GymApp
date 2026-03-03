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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.gymapp.viewmodel.UserUiState
import com.example.gymapp.viewmodel.UserViewModel

private val Background = Color(0xFF0B1220)
private val Surface = Color(0xFF111A2E)
private val Accent = Color(0xFFE53935)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFFAAB3C5)

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

    // ✅ Validacija
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
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Create Profile", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Text(
                            "Set your numbers to calculate programs",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Background
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    Text(
                        text = "Your Basics",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    // Name
                    StyledField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Name",
                        placeholder = "e.g. Djordje",
                        keyboardType = KeyboardType.Text
                    )

                    // Body weight (allow decimal)
                    StyledField(
                        value = bodyWeight,
                        onValueChange = { bodyWeight = filterDecimal(it) },
                        label = "Body Weight (kg)",
                        placeholder = "e.g. 82.5",
                        keyboardType = KeyboardType.Decimal
                    )

                    Divider(color = TextSecondary.copy(alpha = 0.12f))

                    Text(
                        text = "1RM Estimates",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    StyledField(
                        value = squat,
                        onValueChange = { squat = it.filter { c -> c.isDigit() } },
                        label = "Squat 1RM (kg)",
                        placeholder = "e.g. 180",
                        keyboardType = KeyboardType.Number
                    )

                    StyledField(
                        value = bench,
                        onValueChange = { bench = it.filter { c -> c.isDigit() } },
                        label = "Bench 1RM (kg)",
                        placeholder = "e.g. 130",
                        keyboardType = KeyboardType.Number
                    )

                    StyledField(
                        value = deadlift,
                        onValueChange = { deadlift = it.filter { c -> c.isDigit() } },
                        label = "Deadlift 1RM (kg)",
                        placeholder = "e.g. 220",
                        keyboardType = KeyboardType.Number
                    )

                    if (errorText != null) {
                        Text(
                            text = errorText!!,
                            color = Accent,
                            style = MaterialTheme.typography.bodySmall
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
                                bodyWeight = bwValue!! // ✅ novo
                            )

                            // navigacija se radi preko UserUiState (LaunchedEffect gore)
                            isSubmitting = false
                        },
                        enabled = isFormValid && !isSubmitting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Accent,
                            contentColor = TextPrimary,
                            disabledContainerColor = Accent.copy(alpha = 0.35f),
                            disabledContentColor = TextPrimary.copy(alpha = 0.7f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = TextPrimary
                            )
                            Spacer(Modifier.width(10.dp))
                            Text("Saving...")
                        } else {
                            Text("Start Training", fontWeight = FontWeight.Bold)
                        }
                    }

                    Text(
                        text = "You can edit this later in your profile.",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
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
}

/**
 * dozvoljava brojeve i jednu tacku (npr 82.5)
 */
private fun filterDecimal(input: String): String {
    val filtered = input.filter { it.isDigit() || it == '.' }
    val dotCount = filtered.count { it == '.' }
    return if (dotCount <= 1) filtered else filtered.replaceFirst(".", "")
}