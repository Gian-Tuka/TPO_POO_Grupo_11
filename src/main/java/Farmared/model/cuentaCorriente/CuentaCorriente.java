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

    public float getTopeDeuda() {
        return topeDeuda;
    }

    public Float calcularDeuda() {
        return this.deudaActual;
    }

    // Bug 3 — Refactorización: actualiza deudaActual según el tipo de comprobante
    public void agregarComprobante(Comprobante c) {
        comprobantes.add(c);
        if (c instanceof Factura || c instanceof NotaDebito) {
            deudaActual += c.getTotal();
        } else if (c instanceof NotaCredito) {
            deudaActual -= c.getTotal();
        }
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
