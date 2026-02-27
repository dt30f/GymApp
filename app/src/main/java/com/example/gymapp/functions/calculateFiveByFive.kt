package com.example.gymapp.functions

import com.example.gymapp.data.WeekPlan
import com.example.gymapp.data.SetPlan

fun calculateFiveByFive(oneRepMax: Double): List<WeekPlan> {

    val startWeight = (oneRepMax * 0.75).roundToNearest2_5()

    val increment = if (startWeight >= 140) 5.0 else 2.5

    val numberOfTrainings = 16

    return (0 until numberOfTrainings).map { sessionIndex ->

        val weight = (startWeight + sessionIndex * increment)
            .roundToNearest2_5()

        WeekPlan(
            weekNumber = sessionIndex + 1, // sada predstavlja Trening broj
            sets = List(1) { // u Program detail screen pise 5x5
                SetPlan(5, weight)
            }
        )
    }
}

