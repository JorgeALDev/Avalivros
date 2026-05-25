package com.jorge.avalivros.ui.screens.listaLivros

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jorge.avalivros.data.model.Livro
import com.jorge.avalivros.data.repository.AuthRepository
import com.jorge.avalivros.ui.viewmodel.BuscaViewModel
import com.jorge.avalivros.ui.viewmodel.UiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaLivrosScreen(navController: NavController) {
    val viewModel: BuscaViewModel = viewModel()

    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context) }
    val coroutineScope = rememberCoroutineScope()

    var textoPesquisa by remember { mutableStateOf("") }
    val estadoAtual by viewModel.estadoTela.collectAsState()

    val semResultados = estadoAtual is UiState.Sucesso &&
            (estadoAtual as UiState.Sucesso).listaDeLivros.isEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Busca de Livros") },
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            authRepository.limparToken()
                            navController.navigate("login") {
                                popUpTo("lista_livros") { inclusive = true }
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sair"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (semResultados) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = textoPesquisa,
                        onValueChange = { textoPesquisa = it },
                        label = { Text("Pesquisar") },
                        placeholder = { Text("Ex.: Dom Casmurro ou Machado de Assis") },
                        modifier = Modifier.fillMaxWidth(0.7f)
                    )
                    Button(
                        onClick = { viewModel.pesquisarLivros(textoPesquisa) },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(top = 8.dp)
                    ) {
                        Text("Pesquisar")
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, top = 32.dp)
                ) {
                    OutlinedTextField(
                        value = textoPesquisa,
                        onValueChange = { textoPesquisa = it },
                        label = { Text("Digite o nome do livro") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = { viewModel.pesquisarLivros(textoPesquisa) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 16.dp)
                    ) {
                        Text("Pesquisar")
                    }

                    when (val estado = estadoAtual) {
                        is UiState.Carregando -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is UiState.Sucesso -> {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(estado.listaDeLivros) { livro ->
                                    ItemLivro(
                                        livro = livro,
                                        onClick = {
                                            val key = livro.key ?: return@ItemLivro
                                            val keyLimpa = Uri.encode(key.removePrefix("/works/"))
                                            viewModel.selecionarLivro(livro)
                                            navController.navigate("detalhes/$keyLimpa")
                                        }
                                    )
                                    Divider()
                                }
                            }
                        }
                        is UiState.Erro -> {
                            Text(
                                text = estado.mensagem,
                                color = Color.Red,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemLivro(livro: Livro, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = "https://covers.openlibrary.org/b/id/${livro.cover_i}-M.jpg",
            contentDescription = "Capa de ${livro.title}",
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = livro.title, fontWeight = FontWeight.Bold)
            livro.author_name?.let {
                Text(
                    text = "Autor: ${it.joinToString()}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            livro.first_publish_year?.let {
                Text(text = "Ano: $it", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
