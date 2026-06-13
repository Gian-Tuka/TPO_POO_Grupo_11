package Farmared.exception;

public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(String message) {
        super("Usuario no encontrado: " + message);
    }
}
