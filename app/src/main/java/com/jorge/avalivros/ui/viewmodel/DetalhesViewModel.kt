package com.jorge.avalivros.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorge.avalivros.data.model.Livro
import com.jorge.avalivros.data.model.avalivros.AvaliacaoResponse
import com.jorge.avalivros.data.repository.AvalivrosRepository
import com.jorge.avalivros.data.repository.LivroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetalhesUiState {
    object Carregando : DetalhesUiState()
    data class Sucesso(
        val livro: Livro, val avaliacoes: List<AvaliacaoResponse> = emptyList()
    ) : DetalhesUiState()

    data class Erro(val mensagem: String) : DetalhesUiState()
}

class DetalhesViewModel(
    private val livroKey: String,
    private val livroBase: Livro?,
    private val openLibraryRepo: LivroRepository = LivroRepository(),
    private val avaliacoesRepo: AvalivrosRepository = AvalivrosRepository()
) : ViewModel() {

    private val _estado = MutableStateFlow<DetalhesUiState>(DetalhesUiState.Carregando)
    val estado: StateFlow<DetalhesUiState> = _estado

    init {
        carregarDetalhesEAvaliacoes()
    }

    private fun carregarDetalhesEAvaliacoes() {
        viewModelScope.launch {
            _estado.value = DetalhesUiState.Carregando
            try {
                val livroDetalhado =
                    openLibraryRepo.buscarDetalhesPorKey(livroKey)?.let { detalhe ->
                        livroBase?.copy(description = detalhe.description) ?: detalhe
                    } ?: livroBase ?: throw Exception("Livro não encontrado")


                val avaliacoes = avaliacoesRepo.listarAvaliacoes(livroKey)

                _estado.value =
                    DetalhesUiState.Sucesso(livro = livroDetalhado, avaliacoes = avaliacoes)
            } catch (e: Exception) {
                _estado.value = DetalhesUiState.Erro(e.message ?: "Erro ao carregar dados")
            }
        }
    }
}
