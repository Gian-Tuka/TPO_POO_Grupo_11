package Farmared.model.comprobante;

import Farmared.model.ordenCompra.OrdenDeCompra;
import Farmared.model.proveedor.Proveedor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Factura extends Comprobante {
    private OrdenDeCompra ordenDeCompra;
    private List<DetalleComprobante> detalles;
    private TipoFactura tipoFactura;

    public Factura(int nroComprobante, Date fecha, Proveedor proveedor,
                   String descripcion, OrdenDeCompra ordenDeCompra, TipoFactura tipoFactura) {
        super(nroComprobante, fecha, 0f, proveedor, descripcion);
        this.ordenDeCompra = ordenDeCompra;
        this.tipoFactura = tipoFactura;
        this.detalles = new ArrayList<>();
    }

    public void agregarDetalle(DetalleComprobante detalle) {
        this.detalles.add(detalle);
        // Recalcular el monto total cada vez que se agrega un detalle
        this.monto = calcularTotal();
        this.saldoPendiente = this.monto;
    }

    public float calcularSubTotal() {
        float subTotal = 0f;
        for (DetalleComprobante detalle : detalles) {
            subTotal += detalle.getSubTotal();
        }
        return subTotal;
    }

    /**
     * Calcula el total de la factura sumando los subtotales de todos sus detalles.
     * En esta implementación coincide con el subtotal; se puede extender para
     * agregar impuestos u otros cargos.
     */
    public float calcularTotal() {
        return calcularSubTotal();
    }

    @Override
    public float getTotal() {
        return calcularTotal();
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
