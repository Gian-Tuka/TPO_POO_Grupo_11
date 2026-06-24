package Farmared.exception;

public class ComprobanteNoEncontradoException extends RuntimeException {
    public ComprobanteNoEncontradoException(int nroComprobante) {
        super("No se encontró el comprobante con número: " + nroComprobante);
    }
}
