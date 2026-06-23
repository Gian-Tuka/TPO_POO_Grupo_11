package Farmared.exception;

public class UsuarioNoEncontradoException extends FarmaredException {
    public UsuarioNoEncontradoException(String message) {
        super("Usuario no encontrado: " + message);
    }
}
