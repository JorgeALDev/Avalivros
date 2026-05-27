package com.jorge.avalivros.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorge.avalivros.data.repository.AvalivrosRepository
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
        when {
            nota < 1 -> {
                _estado.value = AvaliacaoUiState.Erro("Selecione uma nota entre 1 e 5")
                return
            }
            nota > 5 -> {
                _estado.value = AvaliacaoUiState.Erro("Nota inválida (máximo 5)")
                return
            }
            comentario.isBlank() -> {
                _estado.value = AvaliacaoUiState.Erro("Comentário é obrigatório")
                return
            }
        }
        viewModelScope.launch {
            _estado.value = AvaliacaoUiState.Enviando
            try {
                val response = repository.enviarAvaliacao(livroKey, nota, comentario)
                if (response != null) {
                    _estado.value = AvaliacaoUiState.Sucesso
                } else {
                    _estado.value = AvaliacaoUiState.Erro("Falha ao enviar avaliação. Tente novamente.")
                }
            } catch (e: Exception) {
                _estado.value = AvaliacaoUiState.Erro("Erro de rede: ${e.message}")
            }
        }
    }

    fun resetEstado() {
        _estado.value = AvaliacaoUiState.Pronto
    }
}