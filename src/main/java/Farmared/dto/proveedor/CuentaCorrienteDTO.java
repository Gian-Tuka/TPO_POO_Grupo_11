package Farmared.dto.proveedor;

import Farmared.dto.comprobante.ComprobanteDTO;

import java.util.ArrayList;

public class CuentaCorrienteDTO {
    private float topeDeuda;
    private float deudaActual;
    private ArrayList<ComprobanteDTO> comprobantes;

    public CuentaCorrienteDTO(float topeDeuda, float deudaActual, ArrayList<ComprobanteDTO> comprobantes) {
        this.topeDeuda = topeDeuda;
        this.deudaActual = deudaActual;
        this.comprobantes = comprobantes;
    }

    public float getTopeDeuda() {
        return topeDeuda;
    }

    public void setTopeDeuda(float topeDeuda) {
        this.topeDeuda = topeDeuda;
    }

    public float getDeudaActual() {
        return deudaActual;
    }

    public void setDeudaActual(float deudaActual) {
        this.deudaActual = deudaActual;
    }

    public ArrayList<ComprobanteDTO> getComprobantes() {
        return comprobantes;
    }

    public void setComprobantes(ArrayList<ComprobanteDTO> comprobantes) {
        this.comprobantes = comprobantes;
    }
}
