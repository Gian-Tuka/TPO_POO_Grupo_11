package Farmared.exception;

/**
 * Excepción base del dominio Farmared.
 * Todas las excepciones específicas del dominio extienden de esta clase.
 */
public class FarmaredException extends RuntimeException {
    public FarmaredException(String message) {
        super(message);
    }

    public FarmaredException(String message, Throwable cause) {
        super(message, cause);
    }
}
