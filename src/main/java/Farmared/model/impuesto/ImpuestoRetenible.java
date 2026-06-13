package Farmared.model.impuesto;

import java.util.ArrayList;

public class ImpuestoRetenible {

    private String nroRetencion;
    private String descripcionRetencion;
    private String minimoNoImponible;
    private ArrayList<RangoDeRetencion> rangosDeRetencion;

    public ImpuestoRetenible(String nroRetencion, String descripcionRetencion, ArrayList<RangoDeRetencion> rangosDeRetencion) {
        this.nroRetencion = nroRetencion;
        this.descripcionRetencion = descripcionRetencion;
        this.rangosDeRetencion = rangosDeRetencion;
    }
}
