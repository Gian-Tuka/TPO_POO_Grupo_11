package Farmared.exception;

public class ComprobanteInvalidoException extends FarmaredException {
    public ComprobanteInvalidoException(String motivo) {
        super("Comprobante inválido: " + motivo);
    }
}
