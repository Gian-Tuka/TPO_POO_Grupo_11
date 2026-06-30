package Farmared.model.pago;

import Farmared.exception.FarmaredException;
import Farmared.model.comprobante.Comprobante;
import Farmared.model.comprobante.EstadoComprobante;

public class DetalleCancelacion {
    private Comprobante comprobante;
    private float monto;
    private EstadoCancelacion estado;

    public DetalleCancelacion(Comprobante comprobante, float montoCancelado) {
        this.comprobante = comprobante;
        this.monto = montoCancelado;
        this.estado = validarEstado();
    }


    private EstadoCancelacion validarEstado() {
        if (monto > comprobante.getSaldoPendiente()) {
            throw new FarmaredException(
                    "El monto a cancelar (" + monto + ") supera el saldo pendiente ("
                            + comprobante.getSaldoPendiente() + ") del comprobante "
                            + comprobante.getNroComprobante());
        }
        if (monto == comprobante.getSaldoPendiente()) {
            return EstadoCancelacion.TOTAL;
        }
        return EstadoCancelacion.PARCIAL;
    }

    public void aplicarCancelacion() {
        float nuevoSaldo = comprobante.getSaldoPendiente() - this.monto;
        comprobante.setSaldoPendiente(nuevoSaldo);

        if (nuevoSaldo == 0f) {
            comprobante.setEstado(EstadoComprobante.PAGADO);
        } else {
            comprobante.setEstado(EstadoComprobante.PARCIALMENTE_PAGADO);
        }
    }

    // Getters
    public Comprobante getComprobante() {
        return comprobante;
    }

    public float getMonto() {
        return monto;
    }
    public  EstadoCancelacion getEstado() { return estado; }
    // Setters
    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    public void setMontoCancelado(float montoCancelado) {
        this.monto = montoCancelado;
    }
}
