package com.example.gymapp.data

data class Program(
    val id: String,
    val name: String,
    val exercises: List<ExerciseSet>,
    val description: String,
)

data class ExerciseSet(
    val exercise: String, // "Squat", "Bench", "Deadlift"
    val sets: Int,
    val reps: Int,
    val percentageOf1RM: Int
)
