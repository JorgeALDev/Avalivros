package com.jorge.avalivros.ui.screens.avaliacao

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jorge.avalivros.ui.viewmodel.AvaliacaoUiState
import com.jorge.avalivros.ui.viewmodel.AvaliacaoViewModel
import com.jorge.avalivros.ui.viewmodel.AvaliacaoViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvaliacaoScreen(
    navController: NavController,
    livroKey: String
) {
    // 1. ViewModel – gerencia o estado e a lógica de envio
    val viewModel: AvaliacaoViewModel = viewModel(
        factory = AvaliacaoViewModelFactory(livroKey)
    )
    val estado by viewModel.estado.collectAsState()  // observa mudanças de estado
    val context = LocalContext.current

    // 2. Estados locais da UI (nota e comentário)
    var nota by remember { mutableStateOf(0) }
    var comentario by remember { mutableStateOf("") }

    // 3. Reage a mudanças de estado do ViewModel (sucesso/erro)
    LaunchedEffect(estado) {
        when (estado) {
            is AvaliacaoUiState.Sucesso -> {
                Toast.makeText(context, "Avaliação enviada!", Toast.LENGTH_LONG).show()
                navController.popBackStack()  // volta para tela de detalhes
                viewModel.resetEstado()
            }
            is AvaliacaoUiState.Erro -> {
                Toast.makeText(context, (estado as AvaliacaoUiState.Erro).mensagem, Toast.LENGTH_SHORT).show()
                // erro já foi registrado; ViewModel continua em Pronto? Sim, reset será necessário?
                // O ViewModel não reseta automaticamente; você pode adicionar um reset após erro se quiser.
            }
            else -> { /* nenhuma ação para Pronto ou Enviando */ }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Avaliar Livro") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Sua nota:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            StarRatingBar(
                rating = nota,
                onRatingChanged = { nota = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                label = { Text("Comentário sobre o livro") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (nota == 0) return@Button
                    viewModel.enviarAvaliacao(nota, comentario)
                },
                enabled = nota > 0 && estado !is AvaliacaoUiState.Enviando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (estado is AvaliacaoUiState.Enviando) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Enviar Avaliação")
                }
            }
        }
    }
}

@Composable
fun StarRatingBar(
    maxStars: Int = 5,
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {
    Row {
        for (i in 1..maxStars) {
            val icon: ImageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star
            Icon(
                imageVector = icon,
                contentDescription = "Estrela",
                tint = if (i <= rating) Color(0xFFFFC700) else Color.Gray,
                modifier = Modifier
                    .size(48.dp)
                    .padding(2.dp)
                    .clickable { onRatingChanged(i) }
            )
        }
    }
}