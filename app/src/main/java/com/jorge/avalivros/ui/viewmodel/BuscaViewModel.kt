package com.jorge.avalivros.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorge.avalivros.data.model.Livro
import com.jorge.avalivros.data.repository.LivroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Estados que a tela pode assumir:
sealed class UiState {
    object Carregando : UiState()
    data class Sucesso(val listaDeLivros: List<Livro>) : UiState()
    data class Erro(val mensagem: String) : UiState()
}

class BuscaViewModel : ViewModel() {
    private val repository = LivroRepository()

    // Estado interno (privado)
    private val estado_tela = MutableStateFlow<UiState>(UiState.Sucesso(emptyList()))
    val estadoTela: StateFlow<UiState> = estado_tela

    fun pesquisarLivros(texto: String) {
        if (texto.isBlank()) return

        viewModelScope.launch {
            estado_tela.value = UiState.Carregando

            try {
                val livros = repository.buscarLivros(texto)
                estado_tela.value = UiState.Sucesso(livros)
            } catch (e: Exception) {
                estado_tela.value = UiState.Erro("Falha ao buscar os livros. Verifique sua conexão.")
            }
        }
    }
}