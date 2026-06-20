package Farmared.model.cuentaCorriente;

import Farmared.model.comprobante.Comprobante;

import java.util.ArrayList;

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
}

