package com.example.gymapp.functions

import com.example.gymapp.data.SetPlan
import com.example.gymapp.data.WeekPlan
import kotlin.math.roundToInt

fun calculate531(oneRepMax: Double): List<WeekPlan> {

    val trainingMax = oneRepMax * 0.9

    return listOf(

        WeekPlan(1, listOf(
            SetPlan(5, (trainingMax * 0.65).roundToNearest2_5()),
            SetPlan(5, (trainingMax * 0.75).roundToNearest2_5()),
            SetPlan(5, (trainingMax * 0.85).roundToNearest2_5())
        )),

        WeekPlan(2, listOf(
            SetPlan(3, (trainingMax * 0.70).roundToNearest2_5()),
            SetPlan(3, (trainingMax * 0.80).roundToNearest2_5()),
            SetPlan(3, (trainingMax * 0.90).roundToNearest2_5())
        )),

        WeekPlan(3, listOf(
            SetPlan(5, (trainingMax * 0.75).roundToNearest2_5()),
            SetPlan(3, (trainingMax * 0.85).roundToNearest2_5()),
            SetPlan(1, (trainingMax * 0.95).roundToNearest2_5())
        )),

        WeekPlan(4, listOf(
            SetPlan(5, (trainingMax * 0.40).roundToNearest2_5()),
            SetPlan(5, (trainingMax * 0.50).roundToNearest2_5()),
            SetPlan(5, (trainingMax * 0.60).roundToNearest2_5())
        ))
    )
}
fun Double.roundToNearest2_5(): Double {
    return (this / 2.5).roundToInt() * 2.5
}
