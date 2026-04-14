package com.jorge.avalivros.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jorge.avalivros.data.model.Livro
import com.jorge.avalivros.ui.viewmodel.BuscaViewModel
import com.jorge.avalivros.ui.viewmodel.UiState

@Composable
fun ListaLivrosScreen(viewModel: BuscaViewModel = viewModel()) {
    var textoPesquisa by remember { mutableStateOf("") }
    val estadoAtual by viewModel.estadoTela.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = textoPesquisa,
            onValueChange = { novoTexto ->
                textoPesquisa = novoTexto
            },
            label = { Text("Digite o nome do livro") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.pesquisarLivros(textoPesquisa) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp)
        ) {
            Text("Pesquisar")
        }

        when (val estado = estadoAtual) {
            is UiState.Carregando -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Sucesso -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(estado.listaDeLivros) { livro ->
                        ItemLivro(livro = livro)
                        Divider() // Linha separadora entre os itens
                    }
                }
            }
            is UiState.Erro -> {
                Text(text = estado.mensagem, color = Color.Red)
            }
        }
    }
}

@Composable
fun ItemLivro(livro: Livro) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        // Usamos livro.cover_id porque foi assim que definimos no Modelo
        val urlImagem = "https://covers.openlibrary.org/b/id/${livro.cover_id}-M.jpg"

        AsyncImage(
            model = urlImagem,
            contentDescription = "Capa do livro ${livro.title}",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            // Acessando as propriedades que vieram do JSON
            Text(text = livro.title, fontWeight = FontWeight.Bold)

            livro.author_name?.let { listaAutores ->
                Text(text = "Autor: ${listaAutores.joinToString()}", style = MaterialTheme.typography.bodyMedium)
            }

            livro.first_publish_year?.let { ano ->
                Text(text = "Ano: $ano", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}