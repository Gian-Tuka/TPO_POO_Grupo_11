package Farmared.dto.comprobante;

public class ComprobanteDTO {
    private String tipoComprobante;
    private String numero;
    private String fecha;
    private float montoTotal;

    public ComprobanteDTO(String tipoComprobante, String numero, String fecha, float montoTotal) {
        this.tipoComprobante = tipoComprobante;
        this.numero = numero;
        this.fecha = fecha;
        this.montoTotal = montoTotal;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getNumero() {
        return numero;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public float getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(float montoTotal) {
        this.montoTotal = montoTotal;
    }
}
