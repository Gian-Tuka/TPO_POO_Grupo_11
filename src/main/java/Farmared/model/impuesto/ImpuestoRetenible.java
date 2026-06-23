package Farmared.model.impuesto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImpuestoRetenible {

    private String nroRetencion;
    private String descripcionRetencion;
    private float minimoNoImponible; // Bug 6 — Cambiado de String a float
    private ArrayList<RangoDeRetencion> rangosDeRetencion;

    // Constructor actualizado: incluye minimoNoImponible como float (Bug 6 + Bug 8)
    public ImpuestoRetenible(String nroRetencion, String descripcionRetencion, float minimoNoImponible, ArrayList<RangoDeRetencion> rangosDeRetencion) {
        this.nroRetencion = nroRetencion;
        this.descripcionRetencion = descripcionRetencion;
        this.minimoNoImponible = minimoNoImponible;
        this.rangosDeRetencion = rangosDeRetencion;
    }

    // Bug 6 — Getters faltantes
    public String getNroRetencion() { return nroRetencion; }
    public String getDescripcionRetencion() { return descripcionRetencion; }
    public float getMinimoNoImponible() { return minimoNoImponible; }
    public List<RangoDeRetencion> getRangos() { return Collections.unmodifiableList(rangosDeRetencion); }

    // Bug 8 — Setter para minimoNoImponible
    public void setMinimoNoImponible(float minimoNoImponible) { this.minimoNoImponible = minimoNoImponible; }
}
