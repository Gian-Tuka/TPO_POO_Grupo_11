package Farmared.model.comprobante;

import Farmared.model.proveedor.Proveedor;

import java.util.Date;

public class NotaCredito extends Comprobante {
    private Factura facturaAsociada;

    public NotaCredito(int nroComprobante, Date fecha, float monto,
                       Proveedor proveedor, String descripcion, Factura facturaAsociada) {
        super(nroComprobante, fecha, monto, proveedor, descripcion);
        this.facturaAsociada = facturaAsociada;
    }

    // Getter
    public Factura getFacturaAsociada() {
        return facturaAsociada;
    }
}
