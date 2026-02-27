package com.example.gymapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "lift_entries")
data class LiftEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val liftType: String,   // Bench, Squat, Deadlift
    val weight: Float,
    val date: Long          // timestamp
)
