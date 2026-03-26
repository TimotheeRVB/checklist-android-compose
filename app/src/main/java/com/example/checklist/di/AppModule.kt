package com.example.checklist.di

import android.content.Context
import androidx.room.Room
import com.example.checklist.data.TasksRepository
import com.example.checklist.data.TasksRepositoryImpl
import com.example.checklist.data.UserPreferencesRepository
import com.example.checklist.data.UserPreferencesRepositoryImpl
import com.example.checklist.data.local.TaskDao
import com.example.checklist.data.local.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext applicationContext: Context
    ): TaskDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "tasks_db"
        ).build()
    }

    @Provides
    fun provideTaskDao(
        taskDatabase: TaskDatabase
    ): TaskDao {
        return taskDatabase.taskDao()
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        @ApplicationContext applicationContext: Context
    ): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(applicationContext)
    }

    @Provides
    @Singleton
    fun provideTasksRepository(
        taskDao: TaskDao
    ): TasksRepository {
        return TasksRepositoryImpl(taskDao)
    }
}