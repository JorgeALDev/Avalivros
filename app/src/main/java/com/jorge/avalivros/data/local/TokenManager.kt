package com.jorge.avalivros.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jorge.avalivros.data.model.usuario.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class TokenManager(private val context: Context) {
    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    }


    suspend fun salvarUsuario(usuario: Usuario) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID_KEY] = usuario.id
            prefs[USER_EMAIL_KEY] = usuario.email
        }
    }

    fun obterUsuario(): Flow<Usuario?> {
        return context.dataStore.data.map { prefs ->
            val id = prefs[USER_ID_KEY] ?: return@map null
            Usuario(
                id = id,
                email = prefs[USER_EMAIL_KEY] ?: ""
            )
        }
    }

    suspend fun isLoggedIn(): Boolean {
        return context.dataStore.data.map { prefs ->
            prefs[USER_ID_KEY] != null
        }.first()
    }

    suspend fun limpar() {
        context.dataStore.edit { it.clear() }
    }
}