package Farmared.dto.comprobante;

public class NotaCreditoDTO {
    private String nroComprobante;
    private String fecha;
    private String cuitProveedor;
    private String razonSocialProveedor;
    private String descripcion;
    private float monto;
    private String estado;
    private String nroFacturaAsociada;
    private boolean requiereAutorizacion;

    // Constructor para alta
    public NotaCreditoDTO(String cuitProveedor, String descripcion, float monto, String nroFacturaAsociada) {
        this.cuitProveedor = cuitProveedor;
        this.descripcion = descripcion;
        this.monto = monto;
        this.nroFacturaAsociada = nroFacturaAsociada;
    }

    // Constructor para vista
    public NotaCreditoDTO(String nroComprobante, String fecha, String cuitProveedor, String razonSocialProveedor, String descripcion, float monto, String estado, String nroFacturaAsociada, boolean requiereAutorizacion) {
        this.nroComprobante = nroComprobante;
        this.fecha = fecha;
        this.cuitProveedor = cuitProveedor;
        this.razonSocialProveedor = razonSocialProveedor;
        this.descripcion = descripcion;
        this.monto = monto;
        this.estado = estado;
        this.nroFacturaAsociada = nroFacturaAsociada;
        this.requiereAutorizacion = requiereAutorizacion;
    }

    public String getNroComprobante() { return nroComprobante; }
    public String getFecha() { return fecha; }
    public String getCuitProveedor() { return cuitProveedor; }
    public String getRazonSocialProveedor() { return razonSocialProveedor; }
    public String getDescripcion() { return descripcion; }
    public float getMonto() { return monto; }
    public String getEstado() { return estado; }
    public String getNroFacturaAsociada() { return nroFacturaAsociada; }
    public boolean isRequiereAutorizacion() { return requiereAutorizacion; }

    public void setNroComprobante(String nroComprobante) { this.nroComprobante = nroComprobante; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setCuitProveedor(String cuitProveedor) { this.cuitProveedor = cuitProveedor; }
    public void setRazonSocialProveedor(String razonSocialProveedor) { this.razonSocialProveedor = razonSocialProveedor; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setMonto(float monto) { this.monto = monto; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setNroFacturaAsociada(String nroFacturaAsociada) { this.nroFacturaAsociada = nroFacturaAsociada; }
    public void setRequiereAutorizacion(boolean requiereAutorizacion) { this.requiereAutorizacion = requiereAutorizacion; }
}
