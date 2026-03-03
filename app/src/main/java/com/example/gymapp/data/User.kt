package com.example.gymapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val name: String,
    val squat1RM: Int,
    val bench1RM: Int,
    val deadlift1RM: Int,
    val bodyWeight: Float,
)
