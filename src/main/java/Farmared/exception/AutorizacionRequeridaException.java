package Farmared.exception;

public class AutorizacionRequeridaException extends FarmaredException {
    public AutorizacionRequeridaException(String motivo) {
        super("Autorización requerida: " + motivo);
    }
}
