package com.jorge.avalivros.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jorge.avalivros.data.model.Livro

class DetalhesViewModelFactory(
    private val livroKey: String,
    private val livroBase: Livro? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalhesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetalhesViewModel(livroKey, livroBase) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}