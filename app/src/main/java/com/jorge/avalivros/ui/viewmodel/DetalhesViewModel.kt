package com.jorge.avalivros.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorge.avalivros.data.model.Livro
import com.jorge.avalivros.data.repository.LivroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetalhesUiState {
    object Carregando : DetalhesUiState()
    data class Sucesso(val livro: Livro) : DetalhesUiState()
    data class Erro(val mensagem: String) : DetalhesUiState()
}

class DetalhesViewModel(
    private val livroKey: String,
    private val livroBase: Livro? = null,
    private val repository: LivroRepository = LivroRepository()
) : ViewModel() {

    private val _estado = MutableStateFlow<DetalhesUiState>(DetalhesUiState.Carregando)
    val estado: StateFlow<DetalhesUiState> = _estado

    init {
        carregarDetalhes()
    }

    private fun carregarDetalhes() {
        viewModelScope.launch {
            _estado.value = DetalhesUiState.Carregando
            try {
                val livro = repository.buscarDetalhesPorKey(livroKey, livroBase)
                _estado.value = DetalhesUiState.Sucesso(livro)
            } catch (e: Exception) {
                _estado.value = DetalhesUiState.Erro(e.message ?: "Erro desconhecido")
            }
        }
    }
}