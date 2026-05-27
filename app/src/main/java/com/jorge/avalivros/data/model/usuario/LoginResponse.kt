import com.jorge.avalivros.data.model.usuario.Usuario

data class LoginResponse(
    val message: String,
    val usuario: Usuario
)