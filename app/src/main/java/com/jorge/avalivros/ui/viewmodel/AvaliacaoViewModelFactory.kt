package com.jorge.avalivros.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AvaliacaoViewModelFactory(
    private val livroKey: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AvaliacaoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AvaliacaoViewModel(livroKey) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}