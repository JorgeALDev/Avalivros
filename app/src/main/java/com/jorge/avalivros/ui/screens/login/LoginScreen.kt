package com.jorge.avalivros.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jorge.avalivros.data.repository.AuthRepository
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.jorge.avalivros.ui.theme.AvalivrosTheme

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    val context = LocalContext.current // Pega o contexto do Android
    val coroutineScope = rememberCoroutineScope() // Para rodar funções 'suspend'
    val authRepository = remember { AuthRepository(context) } // Instancia o repositório

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Avalivros", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && senha.isNotBlank()) {
                    // Iniciamos a Coroutine para rodar em segundo plano
                    coroutineScope.launch {
                        // 1. Simulamos que a API retornou um Token
                        val tokenSimulado = "token_ficticio_${email}"

                        // 2. Salvamos o Token no celular
                        authRepository.salvarToken(tokenSimulado)

                        // 3. Navegamos para a Lista de Livros
                        navController.navigate("lista_livros") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        TextButton(onClick = { navController.navigate("cadastro") }) {
            Text("Não tem conta? Cadastre-se")
        }
    }
}


@Preview(showBackground = true) // showBackground pinta o fundo de branco para o preview
@Composable
fun LoginScreenPreview() {
    // Usamos o seu tema para as cores ficarem iguais as do app
    AvalivrosTheme {
        // Criamos um navController "falso" apenas para o Preview não dar erro
        val navControllerFalso = rememberNavController()
        // Chamamos a sua tela
        LoginScreen(navController = navControllerFalso)
    }
}