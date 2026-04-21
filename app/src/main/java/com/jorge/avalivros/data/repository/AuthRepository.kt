package com.jorge.avalivros.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Isso cria o arquivo de banco de dados no celular chamado "user_prefs"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class AuthRepository(private val context: Context) {

    // Essa é a "Chave" que usaremos para guardar e buscar o token
    companion object {
        val TOKEN_KEY = stringPreferencesKey("jwt_token")
    }

    // Função para salvar o token
    suspend fun salvarToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    // Função para ler o token guardado (retorna um Flow, que é um fluxo de dados contínuo)
    val lerToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    // Função para apagar o token (fazer logout)
    suspend fun limparToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }
}