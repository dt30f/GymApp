package com.example.gymapp.functions

import com.example.gymapp.data.SetPlan
import com.example.gymapp.data.WeekPlan
import kotlin.math.roundToInt

enum class LiftType { SQUAT, BENCH, DEADLIFT }

fun calculate531(
    oneRepMax: Double,
    liftType: LiftType
): List<WeekPlan> {

    val baseTrainingMax = oneRepMax * 0.90
    val tmIncrement = when (liftType) {
        LiftType.BENCH -> 2.5
        LiftType.SQUAT, LiftType.DEADLIFT -> 5.0
    }

    val weeks = mutableListOf<WeekPlan>()
    var weekNumber = 1

    // 4 ciklusa * 4 nedelje = 16 nedelja
    for (cycle in 0 until 4) {
        val tm = baseTrainingMax + (cycle * tmIncrement)

        // Week 1: 85% x 5+
        weeks += WeekPlan(
            weekNumber++,
            listOf(SetPlan(5, (tm * 0.85).roundToNearest2_5()))
        )

        // Week 2: 90% x 3+
        weeks += WeekPlan(
            weekNumber++,
            listOf(SetPlan(3, (tm * 0.90).roundToNearest2_5()))
        )

        // Week 3: 95% x 1+
        weeks += WeekPlan(
            weekNumber++,
            listOf(SetPlan(1, (tm * 0.95).roundToNearest2_5()))
        )

        // Week 4: deload (kao ranije)
        weeks += WeekPlan(
            weekNumber++,
            listOf(
                SetPlan(5, (tm * 0.40).roundToNearest2_5()),
                SetPlan(5, (tm * 0.50).roundToNearest2_5()),
                SetPlan(5, (tm * 0.60).roundToNearest2_5())
            )
        )
    }

    return weeks
}

fun Double.roundToNearest2_5(): Double {
    return (this / 2.5).roundToInt() * 2.5
}