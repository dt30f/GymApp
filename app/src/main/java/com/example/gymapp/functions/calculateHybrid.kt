package com.example.gymapp.functions

import com.example.gymapp.data.SetPlan
import com.example.gymapp.data.WeekPlan

fun calculateHybrid(oneRepMax: Double): List<WeekPlan> {

    val topPercentages = listOf(0.88, 0.91, 0.93, 0.95)
    val volumePercentages = listOf(0.80, 0.82, 0.84, 0.86)
    val backoffPercentage = 0.70

    return (0 until 4).map { index ->

        val topWeight = (oneRepMax * topPercentages[index]).roundToNearest2_5()
        val volumeWeight = (oneRepMax * volumePercentages[index]).roundToNearest2_5()
        val backoffWeight = (oneRepMax * backoffPercentage).roundToNearest2_5()

        WeekPlan(
            weekNumber = index + 1,
            sets = listOf(

                // 🔴 Top set
                SetPlan(1, topWeight),

                // 🟡 3x3
                SetPlan(3, volumeWeight),
                SetPlan(3, volumeWeight),
                SetPlan(3, volumeWeight),

                // 🟢 2x6
                SetPlan(6, backoffWeight),
                SetPlan(6, backoffWeight)
            )
        )
    }
}

