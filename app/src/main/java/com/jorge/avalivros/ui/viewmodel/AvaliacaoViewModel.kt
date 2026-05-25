package com.jorge.avalivros.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorge.avalivros.data.repository.AvalivrosRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AvaliacaoViewModel(
    private val livroKey: String,
    private val repository: AvalivrosRepository = AvalivrosRepository()
) : ViewModel() {

    private val _estado = MutableStateFlow<AvaliacaoUiState>(AvaliacaoUiState.Pronto)
    val estado: StateFlow<AvaliacaoUiState> = _estado

    fun enviarAvaliacao(nota: Int, comentario: String) {
        viewModelScope.launch {
            _estado.value = AvaliacaoUiState.Enviando
            try {
                // Simula chamada de rede (delay de 1 segundo)
                delay(1000)
                // Aqui futuramente você chamará a API real: repository.enviar(nota, comentario, livroKey)
                _estado.value = AvaliacaoUiState.Sucesso
            } catch (e: Exception) {
                _estado.value = AvaliacaoUiState.Erro(e.message ?: "Erro ao enviar avaliação")
            }
        }
    }

    fun resetEstado() {
        _estado.value = AvaliacaoUiState.Pronto
    }
}