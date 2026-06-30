package Farmared.model.comprobante;

import Farmared.model.ordenCompra.Autorizacion;
import Farmared.model.proveedor.Proveedor;

import java.util.Date;

public abstract class Comprobante {
    protected String nroComprobante;
    protected Date fecha;
    protected float monto;
    protected float saldoPendiente;
    protected Proveedor proveedor;
    protected EstadoComprobante estado;
    protected String descripcion;
    protected Autorizacion autorizacion;

    public Comprobante(Proveedor proveedor, String descripcion) {
        this.proveedor = proveedor;
        this.descripcion = descripcion;
        this.estado = EstadoComprobante.PENDIENTE;
    }

    /**
     * Retorna el total del comprobante. Las subclases pueden sobreescribir
     * este método si el total se calcula de forma diferente (ej: Factura
     * calcula a partir de sus detalles).
     */
    public float getTotal() {
        return monto;
    }

    // Getters
    public String getNroComprobante() {
        return nroComprobante;
    }

    public Date getFecha() {
        return fecha;
    }

    public float getMonto() {
        return monto;
    }

    public float getSaldoPendiente() {
        return saldoPendiente;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public EstadoComprobante getEstado() {
        return estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Autorizacion getAutorizacion() {
        return autorizacion;
    }

    // Setters
    public void setEstado(EstadoComprobante estado) {
        this.estado = estado;
    }

    public void setSaldoPendiente(float saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }

    public void setAutorizacion(Autorizacion autorizacion) {
        this.autorizacion = autorizacion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
