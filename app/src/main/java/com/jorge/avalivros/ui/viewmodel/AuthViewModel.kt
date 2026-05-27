package com.jorge.avalivros.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorge.avalivros.data.local.TokenManager
import com.jorge.avalivros.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val tokenManager: TokenManager,
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    fun register(email: String, senha: String) {
        when {
            email.isBlank() -> {
                _uiState.value = AuthUiState.Error("E-mail é obrigatório")
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _uiState.value = AuthUiState.Error("E-mail inválido")
                return
            }
            senha.isBlank() -> {
                _uiState.value = AuthUiState.Error("Senha é obrigatória")
                return
            }
            senha.length < 6 -> {
                _uiState.value = AuthUiState.Error("Senha deve ter no mínimo 6 caracteres")
                return
            }
        }
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val usuario = repository.register(email, senha)
            if (usuario != null) {
                tokenManager.salvarUsuario(usuario)
                _uiState.value = AuthUiState.Success(usuario)
            } else {
                _uiState.value = AuthUiState.Error("Erro ao cadastrar. Tente novamente.")
            }
        }
    }

    fun login(email: String, senha: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val response = repository.login(email, senha)
            if (response != null) {
                tokenManager.salvarUsuario(response.usuario)
                _uiState.value = AuthUiState.Success(response.usuario)
            } else {
                _uiState.value = AuthUiState.Error("E-mail ou senha inválidos.")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.limpar()
            _uiState.value = AuthUiState.Idle
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}