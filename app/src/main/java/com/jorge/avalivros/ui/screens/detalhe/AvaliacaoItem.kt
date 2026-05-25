package com.jorge.avalivros.ui.screens.detalhe

import androidx.compose.material3.Card
import androidx.compose.runtime.Composable

@Composable
fun AvaliacaoItem(avaliacao: AvaliacaoResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Exibe estrelas (pequenas) baseadas na nota
                repeat(5) { index ->
                    Icon(
                        imageVector = if (index < avaliacao.nota) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = null,
                        tint = if (index < avaliacao.nota) Color(0xFFFFC700) else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = avaliacao.nota.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = avaliacao.comentario,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}