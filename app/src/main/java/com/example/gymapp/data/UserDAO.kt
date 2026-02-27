package com.example.gymapp.data

import androidx.room.*
import com.example.gymapp.data.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users LIMIT 1")
    fun getUserFlow(): Flow<User?>

    @Update
    suspend fun updateUser(user: User)



}


