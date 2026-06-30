package Farmared.model.comprobante;

import Farmared.model.ordenCompra.DetalleOC;
import Farmared.model.ordenCompra.OrdenDeCompra;
import Farmared.model.proveedor.Proveedor;
import Farmared.utils.GeneradorDeCodigos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Factura extends Comprobante {
    private OrdenDeCompra ordenDeCompra;
    private List<DetalleComprobante> detalles;
    private TipoFactura tipoFactura;
    private float subTotalSinImpuestos;
    private float totalImpuestos;

    public Factura(Proveedor proveedor, String descripcion, OrdenDeCompra ordenDeCompra, TipoFactura tipoFactura) {
        super(proveedor, descripcion);
        this.nroComprobante = generarCodigoFactura();
        this.fecha = obtenerFechaActual();
        this.ordenDeCompra = ordenDeCompra;
        this.tipoFactura = tipoFactura;
        this.detalles = new ArrayList<>();
    }
    private String generarCodigoFactura() {
        GeneradorDeCodigos gdc = new GeneradorDeCodigos();
        return gdc.generarCodigo("FC", 4);
    }

    private Date obtenerFechaActual() {
        return new Date();
    }

    public void agregarDetalle(DetalleComprobante detalle) {
        this.detalles.add(detalle);
    }


    public boolean tieneDesvios() {
        if (this.ordenDeCompra == null) {
            return true;
        }

        for (DetalleComprobante detFactura : this.detalles) {
            boolean encontradoEnOC = false;

            for (DetalleOC detOC : ordenDeCompra.getDetalles()) {
                if (detFactura.getItem().getCodigo().equals(detOC.getItem().getCodigo())) {
                    encontradoEnOC = true;

                    // controlar diferencia de precio unitario (mayor o menor)
                    if (Math.abs(detFactura.getPrecioFacturado() - detOC.getPrecioUnitarioVal()) > 0.01f) {
                        return true;
                    }
                    break;
                }
            }
            // Control de productos informados: Si el item de la factura no existía en la OC -> Desvío
            if (!encontradoEnOC) {
                return true;
            }
        }
        return false; // Pasó todos los controles sin desvíos
    }

    public void calcularTotalesYSubtotales() {
        float acumuladorSubtotal = 0f;
        float acumuladorImpuestos = 0f;

        for (DetalleComprobante detalle : detalles) {
            acumuladorSubtotal += detalle.getSubTotal();
            float porcentajeIva = extraerPorcentajeIva(detalle.getItem().getTipoDeIVA().name());
            acumuladorImpuestos += detalle.getSubTotal() * (porcentajeIva / 100f);
        }

        this.subTotalSinImpuestos = acumuladorSubtotal;
        this.totalImpuestos = acumuladorImpuestos;
        this.monto = acumuladorSubtotal + acumuladorImpuestos;
        this.saldoPendiente = this.monto;
    }

    private float extraerPorcentajeIva(String tipoIva) {
        if ("IVA_21".equals(tipoIva)) return 21f;
        if ("IVA_10_5".equals(tipoIva)) return 10.5f;
        if ("IVA_27".equals(tipoIva)) return 27f;
        return 0f;
    }

    // Getters
    public OrdenDeCompra getOrdenDeCompra() {
        return ordenDeCompra;
    }

    public List<DetalleComprobante> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    public TipoFactura getTipoFactura() {
        return tipoFactura;
    }
}
