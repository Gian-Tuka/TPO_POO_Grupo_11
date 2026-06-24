package Farmared.dto.impuesto;

public class ImpuestoRetenibleDTO {
    private String nroRetencion;
    private String descripcionRetencion;
    private String minimoNoImponible;

    public ImpuestoRetenibleDTO(String nroRetencion, String descripcionRetencion, String minimoNoImponible) {
        this.nroRetencion = nroRetencion;
        this.descripcionRetencion = descripcionRetencion;
        this.minimoNoImponible = minimoNoImponible;
    }

    public String getNroRetencion() {
        return nroRetencion;
    }

    public void setNroRetencion(String nroRetencion) {
        this.nroRetencion = nroRetencion;
    }

    public String getDescripcionRetencion() {
        return descripcionRetencion;
    }

    public void setDescripcionRetencion(String descripcionRetencion) {
        this.descripcionRetencion = descripcionRetencion;
    }

    public String getMinimoNoImponible() {
        return minimoNoImponible;
    }

    public void setMinimoNoImponible(String minimoNoImponible) {
        this.minimoNoImponible = minimoNoImponible;
    }

    @Override
    public String toString() {
        return descripcionRetencion + " (Nro: " + nroRetencion + ")";
    }
}
