package com.jorge.avalivros.ui.viewmodel

sealed class AvaliacaoUiState {
    object Pronto : AvaliacaoUiState()
    object Enviando : AvaliacaoUiState()
    object Sucesso : AvaliacaoUiState()
    data class Erro(val mensagem: String) : AvaliacaoUiState()
}