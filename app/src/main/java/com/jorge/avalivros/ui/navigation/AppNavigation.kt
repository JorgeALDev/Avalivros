package com.jorge.avalivros.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jorge.avalivros.ui.screens.avaliacao.AvaliacaoScreen
import com.jorge.avalivros.ui.screens.cadastro.CadastroScreen
import com.jorge.avalivros.ui.screens.detalhe.DetalhesLivroScreen
import com.jorge.avalivros.ui.screens.listaLivros.ListaLivrosScreen
import com.jorge.avalivros.ui.screens.login.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("cadastro") {
            CadastroScreen(navController = navController)
        }
        composable("lista_livros") {
            ListaLivrosScreen(navController = navController)
        }
        composable(
            route = "detalhes/{livroKey}",
            arguments = listOf(navArgument("livroKey") { type = NavType.StringType })
        ) { backStackEntry ->
            val livroKey = backStackEntry.arguments?.getString("livroKey") ?: return@composable
            DetalhesLivroScreen(navController = navController, livroKey = livroKey)
        }
        composable("avaliacao/{livroKey}") { backStackEntry ->
            val livroKey = backStackEntry.arguments?.getString("livroKey") ?: return@composable
            AvaliacaoScreen(navController, livroKey)
        }
    }
}