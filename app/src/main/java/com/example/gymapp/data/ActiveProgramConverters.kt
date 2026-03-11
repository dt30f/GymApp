package com.example.gymapp.data

import androidx.room.TypeConverter
import org.json.JSONArray
import org.json.JSONObject

class ActiveProgramConverters {

    @TypeConverter
    fun fromWeekPlans(value: List<WeekPlan>): String {
        val weeksArray = JSONArray()

        value.forEach { week ->
            val weekObject = JSONObject().apply {
                put("weekNumber", week.weekNumber)
                put(
                    "sets",
                    JSONArray().apply {
                        week.sets.forEach { set ->
                            put(
                                JSONObject().apply {
                                    put("reps", set.reps)
                                    put("weight", set.weight)
                                }
                            )
                        }
                    }
                )
            }
            weeksArray.put(weekObject)
        }

        return weeksArray.toString()
    }

    @TypeConverter
    fun toWeekPlans(value: String): List<WeekPlan> {
        if (value.isBlank()) return emptyList()

        val weeksArray = JSONArray(value)
        return buildList {
            for (index in 0 until weeksArray.length()) {
                val weekObject = weeksArray.getJSONObject(index)
                val setsArray = weekObject.getJSONArray("sets")

                val sets = buildList {
                    for (setIndex in 0 until setsArray.length()) {
                        val setObject = setsArray.getJSONObject(setIndex)
                        add(
                            SetPlan(
                                reps = setObject.getInt("reps"),
                                weight = setObject.getDouble("weight")
                            )
                        )
                    }
                }

                add(
                    WeekPlan(
                        weekNumber = weekObject.getInt("weekNumber"),
                        sets = sets
                    )
                )
            }
        }
    }
}
