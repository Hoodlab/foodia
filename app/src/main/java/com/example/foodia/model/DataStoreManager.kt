package com.example.foodia.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "onBoarding")

class DataStoreManager(context: Context) {
    private object PreferenceKey {
        val key = booleanPreferencesKey("completed")
    }

    private val dataStore = context.dataStore

    suspend fun saveOnBoardingState(isComplete: Boolean) {
        dataStore.edit { pref ->
            pref[PreferenceKey.key] = isComplete
        }
    }

    fun getOnBoardingState(): Flow<Boolean> = dataStore.data
        .catch { error ->
            if (error is IOException) {
                emit(emptyPreferences())
            } else {
                throw error
            }
        }.map { preference ->
            val onboardingState = preference[PreferenceKey.key] ?: false
            onboardingState
        }
}