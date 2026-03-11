package com.example.gymapp.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "active_programs",
    indices = [Index(value = ["userId", "selectedLift"], unique = true)]
)
data class ActiveProgram(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val programId: String,
    val programName: String,
    val selectedLift: String,
    val oneRepMax: Double,
    val startDate: Long,
    val generatedPlan: List<WeekPlan>
)
