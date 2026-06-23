package Farmared.model.cuentaCorriente;

import Farmared.model.comprobante.Comprobante;

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

    public void agregarComprobante(Comprobante comprobante) {
        this.comprobantes.add(comprobante);

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
