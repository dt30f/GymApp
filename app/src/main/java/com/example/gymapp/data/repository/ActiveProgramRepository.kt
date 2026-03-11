package com.example.gymapp.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.gymapp.data.ActiveProgram
import com.example.gymapp.data.ActiveProgramDao
import com.example.gymapp.data.UserDao
import com.example.gymapp.data.WeekPlan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ActiveProgramRepository @Inject constructor(
    private val activeProgramDao: ActiveProgramDao,
    private val userDao: UserDao
) {

    fun observeActivePrograms(): Flow<List<ActiveProgram>> {
        return userDao.getUserFlow().flatMapLatest { user ->
            if (user == null) {
                flowOf(emptyList())
            } else {
                activeProgramDao.getActivePrograms(user.id)
            }
        }
    }

    fun observeActiveProgram(programId: Long): Flow<ActiveProgram?> {
        return activeProgramDao.getActiveProgramById(programId)
    }

    suspend fun startProgram(
        programId: String,
        programName: String,
        selectedLift: String,
        oneRepMax: Double,
        generatedPlan: List<WeekPlan>,
        startDate: Long
    ): StartProgramResult {
        val user = userDao.getUser() ?: return StartProgramResult.NoUser

        val existingProgram = activeProgramDao.getActiveProgramForLift(user.id, selectedLift)
        if (existingProgram != null) {
            return StartProgramResult.LiftAlreadyActive(selectedLift)
        }

        return try {
            val id = activeProgramDao.insertActiveProgram(
                ActiveProgram(
                    userId = user.id,
                    programId = programId,
                    programName = programName,
                    selectedLift = selectedLift,
                    oneRepMax = oneRepMax,
                    startDate = startDate,
                    generatedPlan = generatedPlan
                )
            )
            StartProgramResult.Success(id)
        } catch (_: SQLiteConstraintException) {
            StartProgramResult.LiftAlreadyActive(selectedLift)
        }
    }
}

sealed interface StartProgramResult {
    data class Success(val programRecordId: Long) : StartProgramResult
    data class LiftAlreadyActive(val lift: String) : StartProgramResult
    object NoUser : StartProgramResult
}

