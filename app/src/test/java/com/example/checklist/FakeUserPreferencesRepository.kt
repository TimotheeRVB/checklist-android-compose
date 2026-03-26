package com.example.checklist

import com.example.checklist.data.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserPreferencesRepository(
    initialHideCompleted: Boolean = false
) : UserPreferencesRepository {


    private val hideCompletedFlow = MutableStateFlow(initialHideCompleted)

    override fun observeHideCompleted(): Flow<Boolean> {
        return hideCompletedFlow
    }

    override suspend fun setHideCompleted(hideCompleted: Boolean) {
        hideCompletedFlow.value = hideCompleted
    }
}