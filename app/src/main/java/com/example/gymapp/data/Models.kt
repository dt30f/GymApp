package com.example.gymapp.data

data class WeekPlan(
    val weekNumber: Int,
    val sets: List<SetPlan>
)

data class SetPlan(
    val reps: Int,
    val weight: Double
)
