package Farmared.dto.comprobante;

import java.util.ArrayList;

public class FacturaDTO {
    private int nroComprobante;
    private String fecha;
    private String cuitProveedor;
    private String razonSocialProveedor;
    private String descripcion;
    private float montoTotal;
    private String estado;
    private String nroOC;
    private ArrayList<DetalleComprobanteDTO> detalles;
    private String tipoFactura;

    // Constructor para alta
    public FacturaDTO(String cuitProveedor, String descripcion, String nroOC, ArrayList<DetalleComprobanteDTO> detalles, String tipoFactura) {
        this.cuitProveedor = cuitProveedor;
        this.descripcion = descripcion;
        this.nroOC = nroOC;
        this.detalles = detalles;
        this.tipoFactura = tipoFactura;
    }

    // Constructor para vista
    public FacturaDTO(int nroComprobante, String fecha, String cuitProveedor, String razonSocialProveedor, String descripcion, float montoTotal, String estado, String nroOC, ArrayList<DetalleComprobanteDTO> detalles, String tipoFactura) {
        this.nroComprobante = nroComprobante;
        this.fecha = fecha;
        this.cuitProveedor = cuitProveedor;
        this.razonSocialProveedor = razonSocialProveedor;
        this.descripcion = descripcion;
        this.montoTotal = montoTotal;
        this.estado = estado;
        this.nroOC = nroOC;
        this.detalles = detalles;
        this.tipoFactura = tipoFactura;
    }

    public int getNroComprobante() { return nroComprobante; }
    public String getFecha() { return fecha; }
    public String getCuitProveedor() { return cuitProveedor; }
    public String getRazonSocialProveedor() { return razonSocialProveedor; }
    public String getDescripcion() { return descripcion; }
    public float getMontoTotal() { return montoTotal; }
    public String getEstado() { return estado; }
    public String getNroOC() { return nroOC; }
    public ArrayList<DetalleComprobanteDTO> getDetalles() { return detalles; }
    public String getTipoFactura() { return tipoFactura; }

    public void setNroComprobante(int nroComprobante) { this.nroComprobante = nroComprobante; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setCuitProveedor(String cuitProveedor) { this.cuitProveedor = cuitProveedor; }
    public void setRazonSocialProveedor(String razonSocialProveedor) { this.razonSocialProveedor = razonSocialProveedor; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setMontoTotal(float montoTotal) { this.montoTotal = montoTotal; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setNroOC(String nroOC) { this.nroOC = nroOC; }
    public void setDetalles(ArrayList<DetalleComprobanteDTO> detalles) { this.detalles = detalles; }
    public void setTipoFactura(String tipoFactura) { this.tipoFactura = tipoFactura; }
}
