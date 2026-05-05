package com.jorge.avalivros.ui.screens.detalhe

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jorge.avalivros.ui.viewmodel.BuscaViewModel
import com.jorge.avalivros.ui.viewmodel.DetalhesUiState
import com.jorge.avalivros.ui.viewmodel.DetalhesViewModel
import com.jorge.avalivros.ui.viewmodel.DetalhesViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesLivroScreen(
    navController: NavController,
    livroKey: String
) {
    val listEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry("lista_livros")
    }
    val buscaViewModel: BuscaViewModel = viewModel(listEntry)
    val livroBase by buscaViewModel.livroSelecionado.collectAsState()

    val viewModel: DetalhesViewModel = viewModel(
        key = livroKey,
        factory = DetalhesViewModelFactory(livroKey, livroBase)
    )
    val estado by viewModel.estado.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Livro") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        when (estado) {
            is DetalhesUiState.Carregando -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is DetalhesUiState.Sucesso -> {
                val livro = (estado as DetalhesUiState.Sucesso).livro
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (livro.cover_i != null) {
                        AsyncImage(
                            model = "https://covers.openlibrary.org/b/id/${livro.cover_i}-M.jpg",
                            contentDescription = "Capa de ${livro.title}",
                            modifier = Modifier.size(200.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier.size(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Sem imagem", color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = livro.title,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    livro.author_name?.let {
                        Text(text = "Autor(es): ${it.joinToString()}")
                    }
                    livro.first_publish_year?.let {
                        Text(text = "Ano de publicação: $it")
                    }

                    if (!livro.description.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Sinopse",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = livro.description.replace(Regex("\\[.*?]"), "").trim(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Sinopse não disponível.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = { /* TODO: AvaliacaoScreen */ }) {
                        Text("Avaliar este livro")
                    }
                }
            }
            is DetalhesUiState.Erro -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Erro: ${(estado as DetalhesUiState.Erro).mensagem}",
                        color = Color.Red
                    )
                }
            }
        }
    }
}