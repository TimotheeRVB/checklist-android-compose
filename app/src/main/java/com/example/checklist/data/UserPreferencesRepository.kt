package com.example.checklist.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.checklist.domain.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UserPreferencesRepository {
    fun observeHideCompleted(): Flow<Boolean>
    suspend fun setHideCompleted(hideCompleted: Boolean)
}

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepositoryImpl(
    private val context: Context
) : UserPreferencesRepository {
    private object PreferencesKeys {
        val HIDE_COMPLETED = booleanPreferencesKey("hide_completed")
    }

    override suspend fun setHideCompleted(hideCompleted: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HIDE_COMPLETED] = hideCompleted
        }
    }

    override fun observeHideCompleted(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.HIDE_COMPLETED] ?: false
        }
    }
}