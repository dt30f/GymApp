package com.example.gymapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [LiftEntry::class, User::class, ActiveProgram::class], version = 3)
@TypeConverters(ActiveProgramConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun liftDao(): LiftDao
    abstract fun userDao(): UserDao
    abstract fun activeProgramDao(): ActiveProgramDao
}
