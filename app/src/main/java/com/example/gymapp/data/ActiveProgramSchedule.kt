package com.example.gymapp.data

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

private val ScheduleZone: ZoneId = ZoneId.systemDefault()

data class ScheduledWorkoutOccurrence(
    val date: LocalDate,
    val programId: String,
    val programName: String,
    val lift: String,
    val displayTitle: String,
    val rows: List<ScheduledWorkoutRow>
)

data class ScheduledWorkoutRow(
    val sets: Int,
    val reps: Int,
    val weight: Double
)

fun mapActiveProgramToScheduledWorkouts(program: ActiveProgram): List<ScheduledWorkoutOccurrence> {
    val startDate = timestampToLocalDate(program.startDate)

    return program.generatedPlan.mapIndexed { index, weekPlan ->
        val scheduledDate = when (program.programId) {
            "five_by_five" -> startDate.plusDays(index.toLong() * 3L)
            else -> startDate.plusWeeks(index.toLong())
        }

        ScheduledWorkoutOccurrence(
            date = scheduledDate,
            programId = program.programId,
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

fun mapActiveProgramsToScheduledWorkouts(programs: List<ActiveProgram>): List<ScheduledWorkoutOccurrence> {
    return programs.flatMap(::mapActiveProgramToScheduledWorkouts)
}

fun resolveSharedCalendarStartDate(
    existingPrograms: List<ActiveProgram>,
    requestedStartDateMillis: Long
): Long {
    val occupiedDates = mapActiveProgramsToScheduledWorkouts(existingPrograms)
        .map { it.date }
        .toSet()

    var resolvedDate = timestampToLocalDate(requestedStartDateMillis)
    while (resolvedDate in occupiedDates) {
        resolvedDate = resolvedDate.plusDays(1)
    }

    return resolvedDate.atStartOfDay(ScheduleZone).toInstant().toEpochMilli()
}

fun timestampToLocalDate(timestamp: Long): LocalDate {
    return Instant.ofEpochMilli(timestamp).atZone(ScheduleZone).toLocalDate()
}

private fun aggregateWorkoutRows(weekPlan: WeekPlan): List<ScheduledWorkoutRow> {
    return weekPlan.sets
        .groupBy { it.reps to it.weight }
        .map { (key, sets) ->
            ScheduledWorkoutRow(
                sets = sets.size,
                reps = key.first,
                weight = key.second
            )
        }
        .sortedWith(compareByDescending<ScheduledWorkoutRow> { it.weight }.thenByDescending { it.reps })
}
