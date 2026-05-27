package com.jorge.avalivros.ui.viewmodel

import com.jorge.avalivros.data.model.usuario.Usuario

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val usuario: Usuario) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}