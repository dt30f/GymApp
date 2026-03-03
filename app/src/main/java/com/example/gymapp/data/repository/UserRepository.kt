package com.example.gymapp.data.repository

import com.example.gymapp.data.User
import com.example.gymapp.data.UserDao
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

    suspend fun getUser(): User? {
        return userDao.getUser()
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    suspend fun updateBodyWeight(userId: String, newWeight: Float) {
        userDao.updateBodyWeight(userId, newWeight)
    }

}
