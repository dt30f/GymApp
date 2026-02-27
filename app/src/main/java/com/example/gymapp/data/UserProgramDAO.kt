package com.example.gymapp.data


import androidx.room.*

@Dao
interface UserProgramDao {

    @Query("SELECT * FROM user_programs WHERE userId = :userId")
    suspend fun getProgramsForUser(userId: String): List<UserProgram>

    @Query("SELECT * FROM user_programs WHERE userId = :userId AND exercise = :exercise")
    suspend fun getProgramForExercise(userId: String, exercise: String): UserProgram?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgram(program: UserProgram)

    @Update
    suspend fun updateProgram(program: UserProgram)
}
