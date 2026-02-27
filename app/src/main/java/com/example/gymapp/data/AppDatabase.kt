package com.example.gymapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LiftEntry::class, User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun liftDao(): LiftDao
    abstract fun userDao(): UserDao
}
