package Farmared.dto.impuesto;

public class CertificadoNoRetencionDTO {
    private String cuitProveedor;
    private String nroRetencion;
    private String fechaInicio;
    private String fechaVencimiento;

    public CertificadoNoRetencionDTO(String cuitProveedor, String nroRetencion, String fechaInicio, String fechaVencimiento) {
        this.cuitProveedor = cuitProveedor;
        this.nroRetencion = nroRetencion;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getCuitProveedor() {
        return cuitProveedor;
    }

    public void setCuitProveedor(String cuitProveedor) {
        this.cuitProveedor = cuitProveedor;
    }

    public String getNroRetencion() {
        return nroRetencion;
    }

    public void setNroRetencion(String nroRetencion) {
        this.nroRetencion = nroRetencion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}
