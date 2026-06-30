package Farmared.model.cuentaCorriente;

import Farmared.model.comprobante.Comprobante;
import Farmared.model.comprobante.Factura;
import Farmared.model.comprobante.NotaCredito;
import Farmared.model.comprobante.NotaDebito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CuentaCorriente {
    private ArrayList<Comprobante> comprobantes;
    private float topeDeuda;
    private float deudaActual;

    public CuentaCorriente(float topeDeuda) {
        this.topeDeuda = topeDeuda;
        this.deudaActual = 0f;
        this.comprobantes = new ArrayList<>();
    }

    public float getDeudaActual() {
        return deudaActual;
    }

    public void setDeudaActual(float deudaActual) {
        this.deudaActual = deudaActual;
    }

    public float getTopeDeuda() {
        return topeDeuda;
    }

    public void agregarComprobante(Comprobante c) {
        if (!comprobantes.contains(c)) {
            comprobantes.add(c);
        }
    }

    // Bug 3 — Refactorización: actualiza deudaActual según el tipo de comprobante y su saldo pendiente
    public void recalcularDeuda() {
        deudaActual = 0f;
        for (Comprobante c : comprobantes) {
            // Impactan en CC los pendientes, autorizados y parcialmente pagados
            if (c.getEstado() == Farmared.model.comprobante.EstadoComprobante.PENDIENTE || 
                c.getEstado() == Farmared.model.comprobante.EstadoComprobante.AUTORIZADO ||
                c.getEstado() == Farmared.model.comprobante.EstadoComprobante.PARCIALMENTE_PAGADO) {
                if (c instanceof Factura || c instanceof NotaDebito) {
                    deudaActual += c.getSaldoPendiente();
                } else if (c instanceof NotaCredito) {
                    deudaActual -= c.getSaldoPendiente();
                }
            }
        }
        if (deudaActual < 0) deudaActual = 0f;
    }

    // Bug 11 — Setter faltante para topeDeuda
    public void setTopeDeuda(float topeDeuda) {
        if (topeDeuda < 0) throw new IllegalArgumentException("El tope de deuda no puede ser negativo");
        this.topeDeuda = topeDeuda;
    }

    // Getter defensivo para comprobantes (sección 1.3)
    public List<Comprobante> getComprobantes() {
        return Collections.unmodifiableList(comprobantes);
    }
}
