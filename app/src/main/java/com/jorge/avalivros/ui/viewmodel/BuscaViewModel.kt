package com.jorge.avalivros.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorge.avalivros.data.model.Livro
import com.jorge.avalivros.data.repository.LivroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UiState {
    object Carregando : UiState()
    data class Sucesso(val listaDeLivros: List<Livro>) : UiState()
    data class Erro(val mensagem: String) : UiState()
}

class BuscaViewModel : ViewModel() {

    private val repository = LivroRepository()

    private val _estadoTela = MutableStateFlow<UiState>(UiState.Sucesso(emptyList()))
    val estadoTela: StateFlow<UiState> = _estadoTela

    private val _livroSelecionado = MutableStateFlow<Livro?>(null)
    val livroSelecionado: StateFlow<Livro?> = _livroSelecionado

    fun pesquisarLivros(texto: String) {
        if (texto.isBlank()) return
        viewModelScope.launch {
            _estadoTela.value = UiState.Carregando
            try {
                val livros = repository.buscarLivros(texto)
                _estadoTela.value = UiState.Sucesso(livros)
            } catch (e: Exception) {
                _estadoTela.value = UiState.Erro("Falha ao buscar os livros. Verifique sua conexão.")
            }
        }
    }

    fun selecionarLivro(livro: Livro) {
        _livroSelecionado.value = livro
    }
}