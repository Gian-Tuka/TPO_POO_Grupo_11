package Farmared.model.pago;

import java.util.Date;

public abstract class FormaDePago {
    protected float monto;
    protected Date fecha;

    public FormaDePago(float monto, Date fecha) {
        this.monto = monto;
        this.fecha = fecha;
    }

    // Getters
    public float getMonto() {
        return monto;
    }

    public Date getFecha() {
        return fecha;
    }

    // Setters
    public void setMonto(float monto) {
        this.monto = monto;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
