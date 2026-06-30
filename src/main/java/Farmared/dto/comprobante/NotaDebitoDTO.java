package Farmared.dto.comprobante;

public class NotaDebitoDTO {
    private String nroComprobante;
    private String fecha;
    private String cuitProveedor;
    private String razonSocialProveedor;
    private String descripcion;
    private float monto;
    private String estado;
    private String nroFacturaAsociada;

    // Constructor para alta (con o sin factura asociada)
    public NotaDebitoDTO(String cuitProveedor, String descripcion, float monto, String nroFacturaAsociada) {
        this.cuitProveedor = cuitProveedor;
        this.descripcion = descripcion;
        this.monto = monto;
        this.nroFacturaAsociada = nroFacturaAsociada != null ? nroFacturaAsociada : "";
    }

    // Constructor para vista/listado
    public NotaDebitoDTO(String nroComprobante, String fecha, String cuitProveedor, String razonSocialProveedor, String descripcion, float monto, String estado) {
        this.nroComprobante = nroComprobante;
        this.fecha = fecha;
        this.cuitProveedor = cuitProveedor;
        this.razonSocialProveedor = razonSocialProveedor;
        this.descripcion = descripcion;
        this.monto = monto;
        this.estado = estado;
        this.nroFacturaAsociada = "";
    }

    public String getNroComprobante() { return nroComprobante; }
    public String getFecha() { return fecha; }
    public String getCuitProveedor() { return cuitProveedor; }
    public String getRazonSocialProveedor() { return razonSocialProveedor; }
    public String getDescripcion() { return descripcion; }
    public float getMonto() { return monto; }
    public String getEstado() { return estado; }
    public String getNroFacturaAsociada() { return nroFacturaAsociada; }

    public void setNroComprobante(String nroComprobante) { this.nroComprobante = nroComprobante; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setCuitProveedor(String cuitProveedor) { this.cuitProveedor = cuitProveedor; }
    public void setRazonSocialProveedor(String razonSocialProveedor) { this.razonSocialProveedor = razonSocialProveedor; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setMonto(float monto) { this.monto = monto; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setNroFacturaAsociada(String nroFacturaAsociada) { this.nroFacturaAsociada = nroFacturaAsociada; }
}
