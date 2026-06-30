package Farmared.model.pago;

import java.util.Date;

public abstract class FormaDePago {
    protected float monto;
    protected Date fecha;

    public FormaDePago(float monto) {
        this.monto = monto;
        this.fecha = generarFecha();
    }

    // Getters
    public float getMonto() {
        return monto;
    }

    public Date getFecha() {
        return fecha;
    }
    private Date generarFecha() {
        return new Date();
    }
    // Setters
    public void setMonto(float monto) {
        this.monto = monto;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
