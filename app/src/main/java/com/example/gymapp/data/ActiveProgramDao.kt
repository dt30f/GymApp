package com.example.gymapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActiveProgramDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertActiveProgram(program: ActiveProgram): Long

    @Query("SELECT * FROM active_programs WHERE userId = :userId ORDER BY startDate DESC")
    fun getActivePrograms(userId: String): Flow<List<ActiveProgram>>

    @Query("SELECT * FROM active_programs WHERE id = :programId LIMIT 1")
    fun getActiveProgramById(programId: Long): Flow<ActiveProgram?>

    @Query("SELECT * FROM active_programs WHERE userId = :userId AND selectedLift = :lift LIMIT 1")
    suspend fun getActiveProgramForLift(userId: String, lift: String): ActiveProgram?
}
