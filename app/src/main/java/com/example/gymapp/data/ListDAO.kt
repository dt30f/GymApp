package com.example.gymapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LiftDao {

    @Insert
    suspend fun insertLift(entry: LiftEntry)

    @Query("SELECT * FROM lift_entries WHERE liftType = :type ORDER BY date ASC")
    fun getLiftsForType(type: String): Flow<List<LiftEntry>>

    @Query("""
    SELECT weight FROM lift_entries 
    WHERE liftType = :type 
    ORDER BY date DESC 
    LIMIT 1
""")
    fun mostRecentLiftWeight(type: String): Flow<Float?>


}
