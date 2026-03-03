package com.example.gymapp.viewmodel

import android.app.Application
import androidx.collection.emptyLongSet
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.LiftDao
import com.example.gymapp.data.LiftEntry
import com.example.gymapp.data.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiftViewModel @Inject constructor(
    private val liftDao: LiftDao,
    private val userDao: UserDao,
) : ViewModel() {

    // StateFlow za Compose
    fun getLifts(type: String): Flow<List<LiftEntry>> {
        return liftDao.getLiftsForType(type)
    }


    fun addLift(type: String, weight: Float) {
        viewModelScope.launch {
            // Insert lift svakako
            liftDao.insertLift(
                LiftEntry(
                    liftType = type,
                    weight = weight,
                    date = System.currentTimeMillis()
                )
            )

            val user = userDao.getUser()

            user?.let {

                val currentPr = when (type) {
                    "Bench" -> it.bench1RM
                    "Squat" -> it.squat1RM
                    else -> it.deadlift1RM
                }

                // ✅ Ako je novi lift veći od PR-a → update PR
                if (weight > currentPr) {
                    val updatedUser = when (type) {
                        "Bench" -> user.copy(bench1RM = weight.toInt())
                        "Squat" -> user.copy(squat1RM = weight.toInt())
                        else -> user.copy(deadlift1RM = weight.toInt())
                    }
                    userDao.updateUser(updatedUser)
                }
            }
        }
    }


    fun getBodyWeight(): Flow<Float> {
        return userDao.getUserFlow().map { user ->
            user?.bodyWeight ?: 0f
        }
    }

    fun getPrForLift(type: String): Flow<Int> {
        return userDao.getUserFlow().map { user ->
            when (type) {
                "Bench" -> user?.bench1RM ?: 0
                "Squat" -> user?.squat1RM ?: 0
                else -> user?.deadlift1RM ?: 0
            }
        }
    }

    fun getMostRecentLift(type: String): Flow<Float> {
        return liftDao.mostRecentLiftWeight(type)
            .map { it ?: 0f }
    }


}

