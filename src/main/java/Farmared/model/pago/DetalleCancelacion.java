package Farmared.model.pago;

import Farmared.model.comprobante.Comprobante;

public class DetalleCancelacion {
    private Comprobante comprobante;
    private float montoCancelado;

    public DetalleCancelacion(Comprobante comprobante, float montoCancelado) {
        this.comprobante = comprobante;
        this.montoCancelado = montoCancelado;
    }

    // Getters
    public Comprobante getComprobante() {
        return comprobante;
    }

    public float getMontoCancelado() {
        return montoCancelado;
    }

    // Setters
    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    public void setMontoCancelado(float montoCancelado) {
        this.montoCancelado = montoCancelado;
    }
}
