package Farmared.model.comprobante;

import Farmared.model.proveedor.Proveedor;
import Farmared.utils.GeneradorDeCodigos;

import java.util.Date;

public class NotaDebito extends Comprobante {

    public NotaDebito(Proveedor proveedor, String descripcion, float montoDebito) {
        super(proveedor, descripcion);

        this.nroComprobante = generarCodigoND();
        this.fecha = obtenerFechaActual();
        this.monto = montoDebito;
        this.saldoPendiente = montoDebito;
    }

    private String generarCodigoND() {
        GeneradorDeCodigos gdc = new GeneradorDeCodigos();
        return gdc.generarCodigo("ND", 4); // Retorna "ND-XXXXXX"
    }

    private Date obtenerFechaActual() {
        return new Date();
    }
}
