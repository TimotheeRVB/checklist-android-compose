package com.example.checklist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.checklist.data.local.TaskDao

@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}