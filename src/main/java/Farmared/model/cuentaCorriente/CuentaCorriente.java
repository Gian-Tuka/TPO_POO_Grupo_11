package Farmared.model.cuentaCorriente;

import Farmared.model.comprobante.Comprobante;

import java.util.ArrayList;

public class CuentaCorriente {
    private ArrayList<Comprobante> comprobantes;
    private float topeDeuda;
    private float deudaActual;






    public CuentaCorriente(float topeDeuda) {
        this.topeDeuda = topeDeuda;
    }


    public float getDeudaActual() {
        return deudaActual;
    }

    public float getTopeDeuda() {
        return topeDeuda;
    }

    private float calcularDeuda(){
        return 2;
    }

    private void agregarComprobante(Comprobante comprobante){}



}

