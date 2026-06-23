package Farmared.exception;

public class TopeDeudaExcedidoException extends FarmaredException {
    public TopeDeudaExcedidoException(float deudaActual, float montoNuevo, float tope) {
        super("Tope de deuda excedido. Deuda actual: " + deudaActual 
            + ", monto nuevo: " + montoNuevo 
            + ", tope: " + tope);
    }
}
