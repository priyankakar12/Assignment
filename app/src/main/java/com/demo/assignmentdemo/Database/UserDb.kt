package com.demo.assignmentdemo.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserModelRoomDb::class],version = 1,exportSchema = false)
abstract class UserDb :RoomDatabase() {
    abstract fun getUserDao(): UserDao
    companion object{

        private const val DB_NAME = "user_db.db"
        @Volatile private var instance: UserDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UserDb::class.java,
            DB_NAME
        ).build()
    }
}