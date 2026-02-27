package com.example.gymapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_programs")
data class UserProgram(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val exercise: String, // "Squat" / "Bench" / "Deadlift"
    val programName: String,
    val startDate: Long,
    val currentWeek: Int,
    val currentWeight: Int
)
