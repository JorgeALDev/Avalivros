package com.jorge.avalivros.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class AuthRepository(private val context: Context) {

    companion object {
        val TOKEN_KEY = stringPreferencesKey("jwt_token")
    }

    suspend fun salvarToken(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }

    val lerToken: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }

    suspend fun limparToken() {
        context.dataStore.edit { it.remove(TOKEN_KEY) }
    }
}