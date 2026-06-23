package Farmared.model.pago;

import Farmared.model.proveedor.Proveedor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class OrdenDePago {
    private int nroOP;
    private Date fechaEmision;
    private Proveedor proveedor;
    private float totalBruto;
    private float totalRetenciones;
    private float totalNeto;
    private List<DetalleCancelacion> detallesCancelacion;
    private List<FormaDePago> formasDePago;

    public OrdenDePago(int nroOP, Date fechaEmision, Proveedor proveedor) {
        this.nroOP = nroOP;
        this.fechaEmision = fechaEmision;
        this.proveedor = proveedor;
        this.totalBruto = 0f;
        this.totalRetenciones = 0f;
        this.totalNeto = 0f;
        this.detallesCancelacion = new ArrayList<>();
        this.formasDePago = new ArrayList<>();
    }

    public void agregarDetalleCancelacion(DetalleCancelacion detalle) {
        this.detallesCancelacion.add(detalle);
        this.totalBruto += detalle.getMontoCancelado();
        calcularTotalNeto();
    }

    public void agregarFormaDePago(FormaDePago formaDePago) {
        this.formasDePago.add(formaDePago);
    }

    public float calcularTotalNeto() {
        this.totalNeto = this.totalBruto - this.totalRetenciones;
        return this.totalNeto;
    }

    // Getters
    public int getNroOP() {
        return nroOP;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public float getTotalBruto() {
        return totalBruto;
    }

    public float getTotalRetenciones() {
        return totalRetenciones;
    }

    public float getTotalNeto() {
        return totalNeto;
    }

    public List<DetalleCancelacion> getDetallesCancelacion() {
        return Collections.unmodifiableList(detallesCancelacion);
    }

    public List<FormaDePago> getFormasDePago() {
        return Collections.unmodifiableList(formasDePago);
    }

    // Setters
    public void setTotalRetenciones(float totalRetenciones) {
        this.totalRetenciones = totalRetenciones;
        calcularTotalNeto();
    }
}
