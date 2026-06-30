package Farmared.model.comprobante;

import Farmared.model.proveedor.Proveedor;
import Farmared.utils.GeneradorDeCodigos;

import java.util.Date;

public class NotaCredito extends Comprobante {
    private Factura facturaAsociada;

    public NotaCredito(Proveedor proveedor, String descripcion, Factura facturaAsociada, float montoCredito) {
        super(proveedor, descripcion);

        this.nroComprobante = generarCodigoNC();
        this.fecha = obtenerFechaActual();
        this.facturaAsociada = facturaAsociada;
        this.monto = montoCredito;
        this.saldoPendiente = montoCredito;
    }

    private String generarCodigoNC() {
        GeneradorDeCodigos gdc = new GeneradorDeCodigos();
        return gdc.generarCodigo("NC", 4); // Retorna "NC-XXXXXX"
    }

    private Date obtenerFechaActual() {
        return new Date();
    }

    public Factura getFacturaAsociada() { return facturaAsociada; }
}
