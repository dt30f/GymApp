package com.example.gymapp.screens.programs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gymapp.data.ActiveProgram
import com.example.gymapp.data.WeekPlan
import com.example.gymapp.screens.AppAccent
import com.example.gymapp.screens.AppAccentMuted
import com.example.gymapp.screens.AppBackground
import com.example.gymapp.screens.AppCardShape
import com.example.gymapp.screens.AppLargeCardShape
import com.example.gymapp.screens.AppPillShape
import com.example.gymapp.screens.AppSurface
import com.example.gymapp.screens.AppSurfaceRaised
import com.example.gymapp.screens.AppSurfaceStroke
import com.example.gymapp.screens.AppTextMuted
import com.example.gymapp.screens.AppTextPrimary
import com.example.gymapp.screens.AppTextSecondary
import com.example.gymapp.screens.MainScaffold
import com.example.gymapp.viewmodel.ActiveProgramsViewModel
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

private val CalendarZone: ZoneId = ZoneId.systemDefault()
private val MonthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
private val LongDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
private val ShortDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM")

@Composable
fun ActiveProgramDetailScreen(
    programId: Long,
    navController: NavController,
    viewModel: ActiveProgramsViewModel = hiltViewModel()
) {
    val activeProgram by viewModel.activeProgram(programId).collectAsState(initial = null)

    MainScaffold(
        navController = navController,
        title = "Program Detail"
    ) { padding ->
        if (activeProgram == null) {
            EmptyProgramState(padding)
        } else {
            val program = activeProgram!!
            val scheduledWorkouts = remember(program) { mapProgramToScheduledWorkouts(program) }
            val workoutsByDate = remember(scheduledWorkouts) { scheduledWorkouts.groupBy { it.date } }
            val today = remember { LocalDate.now(CalendarZone) }
            val todaysWorkouts = workoutsByDate[today].orEmpty()
            var visibleMonth by remember(program.id, program.startDate) {
                mutableStateOf(timestampToLocalDate(program.startDate).let(YearMonth::from))
            }
            var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

            val selectedWorkouts = selectedDate?.let { workoutsByDate[it].orEmpty() }.orEmpty()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppBackground)
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    ActiveProgramHero(program)
                }

                item {
                    ProgramInfoCard(program, scheduledWorkouts.size)
                }

                if (todaysWorkouts.isNotEmpty()) {
                    item {
                        TodaysWorkoutCard(todaysWorkouts)
                    }
                }

                item {
                    CalendarCard(
                        visibleMonth = visibleMonth,
                        workoutsByDate = workoutsByDate,
                        onPreviousMonth = { visibleMonth = visibleMonth.minusMonths(1) },
                        onNextMonth = { visibleMonth = visibleMonth.plusMonths(1) },
                        onDateClick = { date ->
                            val workouts = workoutsByDate[date]
                            if (!workouts.isNullOrEmpty()) {
                                selectedDate = date
                            }
                        }
                    )
                }
            }

            if (selectedDate != null && selectedWorkouts.isNotEmpty()) {
                WorkoutDetailsDialog(
                    date = selectedDate!!,
                    workouts = selectedWorkouts,
                    onDismiss = { selectedDate = null }
                )
            }
        }
    }
}

@Composable
private fun EmptyProgramState(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = AppLargeCardShape,
            colors = CardDefaults.cardColors(containerColor = AppSurface),
            border = BorderStroke(1.dp, AppSurfaceStroke)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Program not found",
                    color = AppTextPrimary,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "This active program could not be loaded.",
                    color = AppTextSecondary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun ActiveProgramHero(program: ActiveProgram) {
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
            Box(
                modifier = Modifier
                    .clip(AppPillShape)
                    .background(AppAccentMuted)
                    .border(1.dp, AppSurfaceStroke, AppPillShape)
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(
                    text = "ACTIVE PROGRAM",
                    color = AppAccent,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = program.programName,
                color = AppTextPrimary,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "Saved for ${program.selectedLift} with calendar-mapped sessions generated from the persisted plan.",
                color = AppTextSecondary,
                style = MaterialTheme.typography.bodyLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HeroMetric(modifier = Modifier.weight(1f), label = "Lift", value = program.selectedLift)
                HeroMetric(modifier = Modifier.weight(1f), label = "1RM", value = "${program.oneRepMax.toInt()} kg")
                HeroMetric(modifier = Modifier.weight(1f), label = "Started", value = shortDate(timestampToLocalDate(program.startDate)))
            }
        }
    }
}

@Composable
private fun ProgramInfoCard(
    program: ActiveProgram,
    workoutCount: Int
) {
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
                text = "Program Snapshot",
                color = AppTextPrimary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            InfoRow("Program", program.programName)
            InfoRow("Lift", program.selectedLift)
            InfoRow("One-rep max", "${program.oneRepMax.toInt()} kg")
            InfoRow("Start date", longDate(timestampToLocalDate(program.startDate)))
            InfoRow("Planned workouts", workoutCount.toString())
        }
    }
}

@Composable
private fun TodaysWorkoutCard(workouts: List<ScheduledWorkout>) {
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
                text = "Today's Workout",
                color = AppTextPrimary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            workouts.forEach { workout ->
                WorkoutSummaryBlock(workout = workout)
            }
        }
    }
}

@Composable
private fun CalendarCard(
    visibleMonth: YearMonth,
    workoutsByDate: Map<LocalDate, List<ScheduledWorkout>>,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDateClick: (LocalDate) -> Unit
) {
    val calendarDays = remember(visibleMonth) { buildCalendarDays(visibleMonth) }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MonthSwitchButton(label = "<", onClick = onPreviousMonth)
                Text(
                    text = visibleMonth.format(MonthFormatter.withLocale(Locale.getDefault())),
                    color = AppTextPrimary,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                MonthSwitchButton(label = ">", onClick = onNextMonth)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                weekDayLabels().forEach { label ->
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = label,
                            color = AppTextMuted,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            calendarDays.chunked(7).forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    week.forEach { day ->
                        val workouts = workoutsByDate[day.date].orEmpty()
                        CalendarDayCell(
                            modifier = Modifier.weight(1f),
                            day = day,
                            hasWorkout = workouts.isNotEmpty(),
                            workoutCount = workouts.size,
                            isToday = day.date == LocalDate.now(CalendarZone),
                            onClick = {
                                if (workouts.isNotEmpty()) {
                                    onDateClick(day.date)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    modifier: Modifier = Modifier,
    day: CalendarDay,
    hasWorkout: Boolean,
    workoutCount: Int,
    isToday: Boolean,
    onClick: () -> Unit
) {
    val containerColor = when {
        hasWorkout -> AppAccentMuted
        else -> AppSurfaceRaised
    }
    val borderColor = when {
        isToday -> AppAccent
        else -> AppSurfaceStroke
    }
    val textColor = when {
        day.isInCurrentMonth -> AppTextPrimary
        else -> AppTextMuted
    }

    Column(
        modifier = modifier
            .height(74.dp)
            .clip(AppCardShape)
            .background(containerColor)
            .border(1.dp, borderColor, AppCardShape)
            .clickable(enabled = hasWorkout, onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            modifier = Modifier.fillMaxWidth(),
            color = textColor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isToday || hasWorkout) FontWeight.Bold else FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Clip
        )

        if (hasWorkout) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(AppPillShape)
                        .background(AppAccent)
                )
                Text(
                    text = if (workoutCount == 1) "Workout" else "$workoutCount sessions",
                    color = AppAccent,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun MonthSwitchButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(AppPillShape)
            .background(AppSurfaceRaised)
            .border(1.dp, AppSurfaceStroke, AppPillShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = AppTextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun WorkoutDetailsDialog(
    date: LocalDate,
    workouts: List<ScheduledWorkout>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppSurface,
        shape = AppLargeCardShape,
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = longDate(date),
                    color = AppTextPrimary,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (workouts.size == 1) "Planned workout" else "${workouts.size} planned workouts",
                    color = AppTextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                workouts.forEach { workout ->
                    WorkoutSummaryBlock(workout = workout)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Close",
                    color = AppAccent,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Composable
private fun WorkoutSummaryBlock(workout: ScheduledWorkout) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AppCardShape)
            .background(AppSurfaceRaised)
            .border(1.dp, AppSurfaceStroke, AppCardShape)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = workout.displayTitle,
            color = AppTextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        InfoLine(label = "Program", value = workout.programName)
        InfoLine(label = "Lift", value = workout.lift)
        workout.rows.forEach { row ->
            InfoLine(
                label = if (row.sets == 1) "Set" else "Sets",
                value = "${row.sets} x ${row.reps} @ ${formatWeight(row.weight)} kg"
            )
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = AppTextSecondary,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            color = AppTextPrimary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun HeroMetric(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Column(
        modifier = modifier
            .clip(AppCardShape)
            .background(AppSurfaceRaised)
            .border(1.dp, AppSurfaceStroke, AppCardShape)
            .padding(horizontal = 14.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
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
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AppCardShape)
            .background(AppSurfaceRaised)
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

data class ScheduledWorkout(
    val date: LocalDate,
    val programName: String,
    val lift: String,
    val displayTitle: String,
    val rows: List<WorkoutRow>
)

data class WorkoutRow(
    val sets: Int,
    val reps: Int,
    val weight: Double
)

data class CalendarDay(
    val date: LocalDate,
    val isInCurrentMonth: Boolean
)

private fun mapProgramToScheduledWorkouts(program: ActiveProgram): List<ScheduledWorkout> {
    val startDate = timestampToLocalDate(program.startDate)

    return program.generatedPlan.mapIndexed { index, weekPlan ->
        val scheduledDate = when (program.programId) {
            "five_by_five" -> startDate.plusDays(index.toLong() * 3L)
            else -> startDate.plusWeeks(index.toLong())
        }

        ScheduledWorkout(
            date = scheduledDate,
            programName = program.programName,
            lift = program.selectedLift,
            displayTitle = if (program.programId == "five_by_five") {
                "Workout ${weekPlan.weekNumber}"
            } else {
                "Week ${weekPlan.weekNumber}"
            },
            rows = aggregateWorkoutRows(weekPlan)
        )
    }
}

private fun aggregateWorkoutRows(weekPlan: WeekPlan): List<WorkoutRow> {
    return weekPlan.sets
        .groupBy { it.reps to it.weight }
        .map { (key, sets) ->
            WorkoutRow(
                sets = sets.size,
                reps = key.first,
                weight = key.second
            )
        }
        .sortedWith(compareByDescending<WorkoutRow> { it.weight }.thenByDescending { it.reps })
}

private fun buildCalendarDays(month: YearMonth): List<CalendarDay> {
    val firstOfMonth = month.atDay(1)
    val lastOfMonth = month.atEndOfMonth()
    val calendarStart = firstOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val calendarEnd = lastOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

    return generateSequence(calendarStart) { date ->
        if (date >= calendarEnd) null else date.plusDays(1)
    }
        .plusElement(calendarEnd)
        .map { date -> CalendarDay(date = date, isInCurrentMonth = YearMonth.from(date) == month) }
        .toList()
}

private fun weekDayLabels(): List<String> = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

private fun timestampToLocalDate(timestamp: Long): LocalDate {
    return Instant.ofEpochMilli(timestamp).atZone(CalendarZone).toLocalDate()
}

private fun shortDate(date: LocalDate): String = date.format(ShortDateFormatter.withLocale(Locale.getDefault()))

private fun longDate(date: LocalDate): String = date.format(LongDateFormatter.withLocale(Locale.getDefault()))

private fun formatWeight(weight: Double): String {
    return if (weight % 1.0 == 0.0) weight.toInt().toString() else weight.toString()
}




