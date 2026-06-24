package Farmared.dto.comprobante;

public class ComprobanteDTO {
    private String tipoComprobante;
    private int numero;
    private String fecha;
    private float montoTotal;

    public ComprobanteDTO(String tipoComprobante, int numero, String fecha, float montoTotal) {
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

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
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
