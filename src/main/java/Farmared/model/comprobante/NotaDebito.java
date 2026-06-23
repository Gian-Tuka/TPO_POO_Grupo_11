package Farmared.model.comprobante;

import Farmared.model.proveedor.Proveedor;

import java.util.Date;

public class NotaDebito extends Comprobante {

    public NotaDebito(int nroComprobante, Date fecha, float monto,
                      Proveedor proveedor, String descripcion) {
        super(nroComprobante, fecha, monto, proveedor, descripcion);
    }
}
