package Farmared.dto.comprobante;

public class NotaDebitoDTO {
    private int nroComprobante;
    private String fecha;
    private String cuitProveedor;
    private String razonSocialProveedor;
    private String descripcion;
    private float monto;
    private String estado;

    // Constructor para alta
    public NotaDebitoDTO(String cuitProveedor, String descripcion, float monto) {
        this.cuitProveedor = cuitProveedor;
        this.descripcion = descripcion;
        this.monto = monto;
    }

    // Constructor para vista
    public NotaDebitoDTO(int nroComprobante, String fecha, String cuitProveedor, String razonSocialProveedor, String descripcion, float monto, String estado) {
        this.nroComprobante = nroComprobante;
        this.fecha = fecha;
        this.cuitProveedor = cuitProveedor;
        this.razonSocialProveedor = razonSocialProveedor;
        this.descripcion = descripcion;
        this.monto = monto;
        this.estado = estado;
    }

    public int getNroComprobante() { return nroComprobante; }
    public String getFecha() { return fecha; }
    public String getCuitProveedor() { return cuitProveedor; }
    public String getRazonSocialProveedor() { return razonSocialProveedor; }
    public String getDescripcion() { return descripcion; }
    public float getMonto() { return monto; }
    public String getEstado() { return estado; }

    public void setNroComprobante(int nroComprobante) { this.nroComprobante = nroComprobante; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setCuitProveedor(String cuitProveedor) { this.cuitProveedor = cuitProveedor; }
    public void setRazonSocialProveedor(String razonSocialProveedor) { this.razonSocialProveedor = razonSocialProveedor; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setMonto(float monto) { this.monto = monto; }
    public void setEstado(String estado) { this.estado = estado; }
}
